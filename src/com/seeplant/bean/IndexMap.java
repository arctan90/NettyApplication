package com.seeplant.bean;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.seeplant.util.MyLogger;

/**
 * 管理open_id 和  index的映射表
 * 因为index是依附于应用，所以如果记录open_id，应该使用key(index+appId)-value(index)这种形制
 * 所以这个是不连续的，用HashMap，而不是ArrayList
 * @author yuantao
 *
 */
public class IndexMap {
    /**
     * 一个index对应一个open_id, 并且是排他性的
     * 如果index存在，是不允许覆盖
     */
    private ConcurrentHashMap<String, String> lOpenIdMap 
        = new ConcurrentHashMap<>(); // Key index+appId Value open_id
    private ConcurrentHashMap<String, String> rOpenIdMap 
        = new ConcurrentHashMap<>(); // Key index+appId Value open_id
    
    
    /**
     * key open_id, value 对应的index列表
     */
    private ConcurrentHashMap<String, ArrayList<Long>> lIndexMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ArrayList<Long>> rIndexMap = new ConcurrentHashMap<>();
    /**
     * 用作统计的map Key应用id，value人数
     */
    private ConcurrentHashMap<Integer, Long>staticMap = new ConcurrentHashMap<>();
    
    private static IndexMap instance;
    
    public static IndexMap getInstance() {
        if (instance == null) {
            instance = new IndexMap();
        }
        return instance;
    }
    
    /**
     * 添加open_id
     * @param index key
     * @param open_id value
     * @param eyeMode 左 1 右 2
     * @return
     */
    public boolean addOpenId(Long index, Integer appId, String open_id, int eyeMode) {
        boolean result = true;
        String key = makeKey(index, appId);
        ConcurrentHashMap<String, String> openIdMap = null;
        ConcurrentHashMap<String, ArrayList<Long>> indexMap = null;
        if (eyeMode == 1) {
            openIdMap = lOpenIdMap;
            indexMap = lIndexMap;
        } else if (eyeMode == 2) {
            openIdMap = rOpenIdMap;
            indexMap = rIndexMap;
        } else {
            return false;
        }
        
        if (index == null || appId == null || open_id == null || open_id.equals("")) {
            result = false;
        } else {
            synchronized (this) {
                Long theStatic = staticMap.get(appId);
                if (theStatic == null) {
                    theStatic = new Long(0);
                }
                staticMap.put(appId, theStatic+1L);
                
                openIdMap.put(key, open_id);
                
                ArrayList<Long> theList = indexMap.get(open_id);
                if (theList == null) {
                    theList = new ArrayList<>();
                }
                theList.add(index);
                
                indexMap.put(open_id, theList);
            }
        }
        return result;
    }
    
    /**
     * 查询open_id
     * @param index 
     * @return
     */
    public String findOpenIdByIndex(Long index, Integer appId, Integer eyeMode) {
        String result = null;
        ConcurrentHashMap<String, String> openIdMap = null;
        if (eyeMode == 1) {
            openIdMap = lOpenIdMap;
        } else if (eyeMode == 2) {
            openIdMap = rOpenIdMap;
        } else {
            return null;
        }
        String key = makeKey(index, appId);
        if (index != null) {
            result = openIdMap.get(key);
        }
        return result;
    }
    
    private String makeKey(Long index, Integer appId) {
        return index.toString()+":"+appId.toString();
    }
    
    public Long[] findIndexsByOpen_id(String open_id, Integer eyeMode) {
        ConcurrentHashMap<String, ArrayList<Long>> indexMap = null;
        if (eyeMode == 1) {
            indexMap = lIndexMap;
        } else if (eyeMode == 2) {
            indexMap = rIndexMap;
        } else {
            return new Long[0];
        }
        
        ArrayList<Long> theList = indexMap.get(open_id);
        if (theList != null && theList.size() >= 0) { 
            Long [] result = new Long[theList.size()];
            for (int iter = 0; iter < theList.size(); iter++) {
                result[iter] = theList.get(iter);
            }
            return result;
        } else {
            return new Long[0];
        }
    }
    
    /**
     * 在内存中，deletedIndex位置的内存内容被replaceeIndex的内存内容替换了，
     * replaceeIndex应该删除，deletedIndex对应open_id应该更新
     * 假设原来的映射关系是
     * deletedIndex <=> open_id0
     * replaceeIndex <=> open_id1
     * 1 在openIdMap中 deletedIndex对应的open_id0要替换成open_id1;
     *   replaceeIndex要删掉
     * 2 在indexMap中，open_id0要删掉;
     *   open_id1对应的replaceeIndex要换成deletedIndex
     *   
     *   特殊处理：如果replaceIndex 和 deleteIndex相等，意味着这是最后一个节点
     *   此时只要把openIdMap中 replaceeIndex 删掉即可
     *      只要把indexMap中 open_id0删掉
     * @param replaceeIndex
     * @param deletedIndex
     * @param app_id
     */
    public void updateIndex(long replaceeIndex, long deletedIndex, int appId, int eyeMode) {
        ConcurrentHashMap<String, String> openIdMap = null;
        ConcurrentHashMap<String, ArrayList<Long>> indexMap = null;
        if (eyeMode == 1) {
            openIdMap = lOpenIdMap;
            indexMap = lIndexMap;
        } else if (eyeMode == 2) {
            openIdMap = rOpenIdMap;
            indexMap = rIndexMap;
        } else {
            return;
        }
        synchronized (this) {
            String deleteKey = makeKey(deletedIndex, appId);
            String open_id0 = openIdMap.get(deleteKey);
            String replaceeKey = makeKey(replaceeIndex, appId);
            String open_id1 = openIdMap.get(replaceeKey);
            if (open_id0 == null || open_id1 == null) {
                String temp = "delete_openId";
                if (open_id1 == null && open_id0 == null) {
                    temp = "all";
                } else if (open_id1 == null) {
                    temp = "replacee_openId";
                }
                MyLogger.log("found no open_id [" + temp
                        + "] replaceIndex:" + replaceeIndex 
                        + " deletedIndex:" + deletedIndex 
                        + " appId:"+ appId 
                        + " eyeMode:" + eyeMode);
                return;
            } 
            if (replaceeIndex == deletedIndex) {
                openIdMap.remove(replaceeKey);
                indexMap.remove(open_id0);
                return;
            }
            
            if (open_id1 != null) {
                openIdMap.replace(deleteKey, open_id1);
                openIdMap.remove(replaceeKey);
            }
            
            indexMap.remove(open_id0);
            
            ArrayList<Long> theList = indexMap.get(open_id1);
            if (theList != null) {
                theList.remove(replaceeIndex);
                if (!theList.contains(deletedIndex)) {
                    theList.add(deletedIndex);
                }
            }
        }
    }
    
    public String getServerInfo() {
        StringBuilder sb = new StringBuilder();
        for (Entry<Integer, Long> entry : staticMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" => ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }
}
