package com.seeplant.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

import java.net.URL;

import org.seeplant.myjson.JSONObject;

import com.seeplant.model.HTTPMsgProcessor;
import com.seeplant.util.MyLogger;

public class HTTPHandler extends SimpleChannelInboundHandler<FullHttpRequest>{    

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //消息体
        ByteBuf buff = request.content();
        
        String mString = buff.toString(CharsetUtil.UTF_8);

        //uri
        String uri = request.getUri();
        MyLogger.log("uri:"+uri+" msg:"+mString);
        
        if (uri.indexOf("cer=iris") < 0 && uri.indexOf("maybe=not") < 0) {
            ctx.close();
            return;
        }
        int position = uri.indexOf("?");
        JSONObject jResponse = null;
        
        if (position < 0) {
        
        } else {
            String uriNoParam = uri.substring(0, position);
        
        //headers
//        HttpHeaders headers = request.headers();
//        List<Map.Entry<String, String>> entries = headers.entries();
//        for (Entry<String, String> entry : entries) {
//            System.out.println(entry.getKey()+":"+entry.getValue());
//        }
        
            try {
                jResponse = new HTTPMsgProcessor().process(uriNoParam, buff);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        if (jResponse == null) {
            jResponse = new JSONObject().put("code", -1).put("msg", "啊，网络吓瘫痪了");
        }
        
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        sendJsonResponse(ctx, jResponse, keepAlive);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        MyLogger.log(cause.getMessage());
        ctx.close();
    }
    
//    @Override
//    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        ctx.fireChannelUnregistered();
//    }
    
    private void sendJsonResponse(ChannelHandlerContext ctx, JSONObject msg, boolean isKeepAlive) {
        FullHttpResponse response = 
                new DefaultFullHttpResponse(HTTP_1_1, 
                        OK, 
                        Unpooled.wrappedBuffer(msg.toString().getBytes()));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
         
        if (!isKeepAlive) {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.writeAndFlush(response);
        }
    }
}
