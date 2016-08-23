package com.seeplant.core.handler;

import java.util.concurrent.TimeUnit;
import com.seeplant.model.MessageProcessor;
import com.seeplant.protobuf.Login;
import com.seeplant.protobuf.Protocol;
import com.seeplant.util.MyLogger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 以Protocol buf作为消息体的Handler
 * @author yuantao
 *
 */
public class ProtoBufHandler extends SimpleChannelInboundHandler<Protocol> {
    private String status = "init";
    private Long uid;
    private String userType;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {
        // TODO Auto-generated method stub
        // 放在Netty循环中跑线程
        if (status.equals("init")) {
            ctx.executor().schedule(new ConnectionTerminator(ctx), 3, TimeUnit.SECONDS);
            status = "waiting";
        }
        
        if (msg.hasLogin()) {
            int result = new MessageProcessor(msg, ctx).processLogin(msg.getLogin());
            if (result == 0) {
                Login login = msg.getLogin();
                this.uid = login.getUid();
                this.userType = login.getUserType();
                status = "success";
            } else {
                MyLogger.log(userType + " uid:" + uid + " 登录失败:" + ctx);
            }
        } 
        else {
            ctx.executor().execute(new MessageProcessor(msg, ctx));
        }
    }  
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        MyLogger.log(userType + " uid:" + uid
                + " channel: " + ctx.channel()
                + " tcp：" + ctx 
                + " 断开。");
        if (status.equals("success")) {
            //清理资源
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    	StringBuffer sb = new StringBuffer();
    	sb.append(cause.getMessage()).append("||");
    	for (StackTraceElement element : cause.getStackTrace()) {
    		sb.append(element.toString());
    	}
        MyLogger.warning("[APP]TCP Exception: " + sb.toString());
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MyLogger.log("TCP连接建立，ctx:"
                        + ctx 
                        + " channel:"
                        + ctx.channel());
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
            if (!status.equals("success")) {
                ctx.close();
                status = "failed";
            }  
        }
    } 
}
