package com.seeplant.bean;

import org.seeplant.myjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 课程组织
 * 在老师开启课程（TeacherJoin）的时候实例化这个对象
 * 在老师离开的时候销毁容器
 * @author yuantao
 *
 */
public class ChattingRoom {
    private Long roomId; //房间id，最小粒度，物理连接
    private ChannelGroup chatGroup;
    
    /******************************************************************/
    protected ChattingRoom(Long roomId){
        this.roomId = roomId;
        if (chatGroup == null) {
            chatGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE); 
        }
    }

    protected void wsBroadCast(JSONObject msg){
        TextWebSocketFrame res = new TextWebSocketFrame(msg.toString());
        chatGroup.writeAndFlush(res);
    }

    protected String wsUserJoinin(Long roomId, ChannelHandlerContext chatCtx){
        if(roomId == null || !roomId.equals(this.roomId)) {
            return "roomId非法";
        }
        chatGroup.add(chatCtx.channel());
        return "OK";
    }
    
    protected void wsUserLeft(ChannelHandlerContext ctx) {
        chatGroup.remove(ctx.channel());
    }
    
    protected boolean tryToRelease() {
        if (chatGroup.size() == 0) {
            chatGroup.clear();
            return true;
        }
        return false;
    }
}

