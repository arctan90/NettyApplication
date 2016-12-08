package com.seeplant.model;


import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Base64.Decoder;
import java.util.List;

import org.seeplant.myjson.JSONObject;

import com.irisking.IKAServer;
import com.seeplant.bean.IndexMap;
import com.seeplant.protobuf.Bye;
import com.seeplant.util.MyLogger;
import com.seeplant.util.Version;

import io.netty.buffer.ByteBuf;


public class HTTPMsgProcessor {
    private static final int REC_LENGTH = 1024;
    private static final int ENR_LENGTH = 512;
    private IKAServer server = new IKAServer();
    
    public HTTPMsgProcessor() {
        
    }
    
    public JSONObject process (String uri, ByteBuf request) throws Exception{
        JSONObject result = null;
        if (request == null) {
            return result;
        }
        switch (uri) {
            case "/feature/add": // 完成
                result = addFeature(request.toString());
                break;
            case "/feature/del": // 完成
                result = delFeature(request.toString());
                break;
            case "/feature/match":
                result = matchOne(request.toString());
                break;
            case "/feature/idel": // 完成
                result = matchMore(request.toString());
                break;
            case "/feature/describImage":
                result = describImage(request);
                break;
            case "/api/getSystemInfo":
                result = getSysInfo();
                break;
            case "":
                break;
            default:
                break;
        }
        MyLogger.log("response:"+result.toString());
        return result;
    }
    
    //特征加载
    private JSONObject addFeature(String request){
        JSONObject jObject = new JSONObject(request);
        JSONObject result = new JSONObject();
        int code = 0;
        String msg = "OK";
        Decoder decoder = Base64.getDecoder();
        
        String open_id = jObject.getString("open_id");
        int appId = jObject.getInt("app_id");
        String feature = jObject.getString("feature");
        int eyeMode = jObject.getInt("eyeMode");
        
        byte[] oneFeature = decoder.decode(feature);
        if (oneFeature.length != ENR_LENGTH) {
            code = -2;
            msg = "服务吓瘫痪了";
        } else {
        Long index = server.enrollFeature(oneFeature, appId, eyeMode);
            if (index < 0) {
                code = index.intValue();
                msg = "服务吓瘫痪了";
            } else {
                // 加载成功后在这里
                IndexMap.getInstance().addOpenId(index, appId, open_id, eyeMode);
                result.put("index", index);
            }
        }
        result.put("app_id", appId)
            .put("code", code)
            .put("msg", msg)
            .put("eyeMode", eyeMode)
            .put("open_id", open_id);  
        return result;
    }
    
    /**
     * 删除特征，用传入的open_id查找出对应的index列表
     * @param jObject
     * @return
     */
    public JSONObject delFeature(String request){
        JSONObject jObject = new JSONObject(request);
        JSONObject result = new JSONObject();
        int code = 0;
        String msg = "OK";

        String open_id = jObject.getString("open_id");
        int app_id = jObject.getInt("app_id");
        int eyeMode = jObject.getInt("eyeMode");
        
        Long[] targetIndexs = IndexMap.getInstance().findIndexsByOpen_id(open_id, eyeMode);

        if (targetIndexs.length > 0) {
            Arrays.sort(targetIndexs, new Comparator<Long>() {

                @Override
                public int compare(Long o1, Long o2) {
                    // TODO Auto-generated method stub
                    return o2.compareTo(o1);
                }
            });
            long[] sortedIndexs = new long[targetIndexs.length];
            for (int iter = 0; iter < targetIndexs.length; iter++) {
                sortedIndexs[iter] = targetIndexs[iter].longValue();
            }
            
            long[] replaceeIndexs = server.removeFeature(app_id, eyeMode, sortedIndexs);
            StringBuffer sb = new StringBuffer();
            sb.append("to del target:");
            for (int iter = 0; iter < targetIndexs.length; iter++) {
                sb.append(targetIndexs[iter]);
                sb.append(" replacee:");
                sb.append(replaceeIndexs[iter]);
            }
            MyLogger.log(sb.toString());
            
            code = -1;
            msg = "found no such feature";
            //用返回的索引值做替换映射表
            for (int iter = 0; iter < targetIndexs.length; iter++) {
                long deletedIndex = targetIndexs[iter]; // 被删除的index
                long replaceeIndex = replaceeIndexs[iter]; // 需要更新的index
                if (replaceeIndex < 0) {
                    continue;
                } else {
                  code = 0;
                  msg = "OK";
                }
                IndexMap.getInstance().updateIndex(replaceeIndex, deletedIndex, app_id, eyeMode);
            } 
        }else {
            MyLogger.log("no need to delete");
        }
        result.put("app_id", app_id)
            .put("code", code)
            .put("msg", msg)
            .put("open_id", open_id); 

        return result;
    }
    
    //特征1:1比对
    public JSONObject matchOne(String request){
        JSONObject jObject = new JSONObject(request);
        JSONObject result = new JSONObject();
        int code = 0;
        String msg = "OK";
        
        String open_id = jObject.getString("open_id");
        int app_id = jObject.getInt("app_id");
        List<Object> targetList = jObject.getJSONArray("targetIndex").toList();
        Long[] targetIndex = (Long[])targetList.toArray();
        String feature = jObject.getString("feature");
        int eyeMode = jObject.getInt("eyeMode");
        
        int matchResult = server.match(feature.getBytes(), app_id, eyeMode, targetIndex);
        code = matchResult;
        if (code != 0) {
            msg = "识别不通过";
        }
        
        result.put("app_id", app_id)
        .put("code", code)
        .put("msg", msg)
        .put("open_id", open_id);
        return result;
    }
    
    //特征1:n比对
    private JSONObject matchMore(String request){
        JSONObject jObject = new JSONObject(request);
        JSONObject result = new JSONObject();
        int code = -1;
        String msg = "识别识别";
        Decoder decoder = Base64.getDecoder();
        long resultIndex = -1L;
        
        int app_id = jObject.getInt("app_id");
        String feature = jObject.getString("feature");
        int eyeMode = jObject.getInt("eyeMode");
        int count = jObject.getInt("count");
        
        byte[] featureBytes = decoder.decode(feature);
        if (featureBytes.length != REC_LENGTH * count || feature.length() == 0) {
            MyLogger.warning("feature length illeagle:"+featureBytes.length);
            code = -2;
            msg = "特征长度异常";
        } else {
//            int featureCount = featureBytes.length / REC_LENGTH;
            long matchResult = server.idel(featureBytes, app_id, eyeMode);
            
            if (matchResult < 0) {
                code = (int)matchResult;
                msg = "识别失败";
            } else {
                code = 0;
                msg = "OK";
                resultIndex = matchResult;
                String open_id = IndexMap.getInstance().findOpenIdByIndex(resultIndex, app_id, eyeMode);
                result.put("open_id", open_id);
            }
        }

        result.put("app_id", app_id)
        .put("code", code)
        .put("msg", msg)
        .put("eyeMode", eyeMode)
        .put("index", resultIndex);
        
        return result;
    }
    
    /**
     * 用图像抽特征
     * @return
     */
    private JSONObject describImage(ByteBuf request) {
        int length = request.writerIndex() - request.readerIndex();
        byte[] bytes = new byte[length]; // 传入的Byte数据
        request.getBytes(request.readerIndex(), bytes);
        System.out.println(new String(bytes));
        
        Bye bye = Bye.parseFrom(bytes);
        
        JSONObject result = new JSONObject();
        return result;
    }
    
    /**
     * 获取系统信息
     * @return
     */
    private JSONObject getSysInfo() {
        JSONObject result = new JSONObject();
        result.put("code", 0);
        result.put("version", Version.getInfo());
        result.put("msg", IndexMap.getInstance().getServerInfo());
        return result;
    }
}
