package com.seeplant.core.handler;

import java.util.concurrent.TimeUnit;
import org.seeplant.myjson.JSONObject;

import com.seeplant.bean.ChattingMap;
import com.seeplant.model.WebSocketMsgProcessor;
import com.seeplant.util.MyLogger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame>{
    private String hasLogin = "init"; // 一个链接，如果没有login过，过5秒把他断掉
//    private boolean chatLogin = false;
    private String userType= "unknow";
    private String uid= "unknow";
    private String roomId= "unknow";
    private String clientType = "anonymous";
    private String appType = "unknow";
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // TODO Auto-generated method stub
        if (frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            try {
                JSONObject jObject = new JSONObject(request);
                String msgType = jObject.getString("type");
                if (hasLogin.equals("init")) {
                    ctx.executor().schedule(new ConnectionTerminator(ctx), 5, TimeUnit.SECONDS);
                    hasLogin = "waiting";
                }
                if (msgType.equals("login")) { //学生端login和老师端推题login
                    int result = new WebSocketMsgProcessor(ctx, request).processLogin(jObject);
                    if (result == 0) {
                        hasLogin = "success";
                        userType = jObject.getString("userType");
                        uid = jObject.getString("uid");
                        roomId = jObject.getString("roomId");
                        clientType = "normalWebSock";
                        if (jObject.has("appType")) {
                            appType = jObject.getString("appType");
                        }
                    }
                } 
                else if (hasLogin.equals("success")){ //增强校验，非登录状态不接受任何业务消息
                    ctx.executor().execute(new WebSocketMsgProcessor(ctx, request));
                } else {
                    MyLogger.log("非登录态试探:" + jObject.toString() + " channel:" +ctx.channel());
                    ctx.close();
                }
            } catch (Exception e){
                MyLogger.log("FrameWork error:"+e.getMessage());
                ctx.close();
            }
        }
    }

    private class ConnectionTerminator implements Runnable{
        ChannelHandlerContext ctx;
        public ConnectionTerminator(ChannelHandlerContext ctx) {
            // TODO Auto-generated constructor stub
            this.ctx = ctx;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (!hasLogin.equals("success")) {
                ctx.close();
                hasLogin = "failed";
            }
        }
    } 
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        String mString = userType + " appType:" + appType+" uid:" + uid 
                + " channel:" + ctx.channel()
                + " clientType:" + clientType
                + " roomId:" + roomId
                + " websocket: " + ctx + " left.";
        MyLogger.log(mString);
        //清理资源
        
        ChattingMap.wsLeft(Long.parseLong(roomId), Long.parseLong(uid), ctx);
        ChattingMap.tryToReleaseRoom(Long.parseLong(roomId));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(cause.getMessage()).append("||");
        for (StackTraceElement element : cause.getStackTrace()) {
            sb.append(element.toString());
        }
        MyLogger.warning("[APP]WebSocket Exception: " + sb.toString());
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MyLogger.log("web socket连接建立，ctx:"
                        + ctx 
                        + " channel:"
                        + ctx.channel());
    }
}

