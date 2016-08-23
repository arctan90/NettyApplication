package com.seeplant.bean;

import com.seeplant.util.ChatFilter;
import com.seeplant.util.MyLogger;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.ConcurrentHashMap;
import org.seeplant.myjson.JSONObject;
/**
 * @author yuantao
 *
 */

public class ChattingMap {
    private static ConcurrentHashMap<Long, ChattingRoom>map = new ConcurrentHashMap<>();

    /********下面是给聊天用的**************/
    public static String  wsChatJoinIn(Long uid, 
            Long roomId, ChannelHandlerContext ctx, 
            String nickName) {
        String result = "OK";
        
        ChattingRoom chattingRoom = map.get(roomId);
        if (chattingRoom == null) {
            chattingRoom = new ChattingRoom(roomId);
            map.put(roomId, chattingRoom);
        }
        
        result = chattingRoom.wsUserJoinin(roomId, ctx);
        return result;
    }
    
   
    public static void wsLeft(Long roomId, Long uid, ChannelHandlerContext ctx) {
        ChattingRoom chattingRoom = map.get(roomId);
        if (chattingRoom != null) {
            chattingRoom.wsUserLeft(ctx);
        }
    }
    
    public static void wsChatting(JSONObject jObject, ChannelHandlerContext ctx) {
        Long roomId = new Long(jObject.getString("roomId"));
        String chattingMsg = jObject.getString("msg");
        String userType = jObject.getString("userType");
        Long uid = jObject.getLong("uid");
        
        ChatFilter filter = new ChatFilter();
        String filtered = filter.filte(chattingMsg, uid, userType);
        if (filtered.isEmpty()) {
            return;
        }
        jObject.put("msg", filtered);
        ChattingRoom chattingRoom = map.get(roomId);
        if (chattingRoom != null) {
            jObject.put("time", System.currentTimeMillis());
            chattingRoom.wsBroadCast(jObject);
        }
    }
    
    public static void tryToReleaseRoom(Long roomId) {
        ChattingRoom chattingRoom = map.get(roomId);
        if (chattingRoom != null) {
            if (chattingRoom.tryToRelease()) {
                MyLogger.log("释放房间:" + roomId);
                map.remove(roomId);
            }
        }
    }

}
