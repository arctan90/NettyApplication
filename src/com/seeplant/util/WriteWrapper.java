package com.seeplant.util;

import org.seeplant.myjson.JSONObject;

import com.seeplant.protobuf.Bye;
import com.seeplant.protobuf.Ping;
import com.seeplant.protobuf.Protocol;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 本类用来封装Protocol消息，只提供一个静态方法write
 * @author yuantao
 *
 */
public class WriteWrapper {
    public static void write(ChannelHandlerContext ctx, Object content){
        if (ctx == null || !ctx.channel().isActive()) {
            return;
        }
        String objectType = content.getClass().getSimpleName();
        Protocol protocol = null;
        byte[] command = new byte[2];
        if (objectType.equals("Ping")) {
            protocol = Protocol.newBuilder().setPing((Ping)content).build();
            command[0] = 0x00;
            command[1] = 0x02;
        } else if (objectType.equals("Bye")) {
            protocol = Protocol.newBuilder().setBye((Bye)content).build();
            command[0] = 0x00;
            command[1] = 0x01;
        }

        if (protocol != null) {
            MyMessageLite messageLite = new MyMessageLite(command, protocol);
            if (ctx.channel().isActive()) {
                ChannelFuture future = ctx.writeAndFlush(messageLite);
                try {
                    future.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            // TODO Auto-generated method stub
                            if (!future.isSuccess()) {
                                MyLogger.log(future.cause().getMessage());
                            }
                        }
                    });
                } catch(Exception e) {
                    MyLogger.log(e.getMessage());
                }
            }
        }
    }
    
    public static void writeWebSocket(ChannelHandlerContext ctx, JSONObject jObject) {
        TextWebSocketFrame res = new TextWebSocketFrame(jObject.toString());
        if (ctx != null && ctx.channel().isActive()) {
        	MyLogger.log("websocket写:"+jObject.toString());
            ctx.writeAndFlush(res);
        }
    }
    
    public static void writeWebSocket(ChannelHandlerContext ctx, String msg) {
        TextWebSocketFrame res = new TextWebSocketFrame(msg);
        if (ctx != null && ctx.channel().isActive()) {
        	MyLogger.log("websocket写:"+msg);
            ctx.writeAndFlush(res);
        }
    }
}
