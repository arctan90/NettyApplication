package com.seeplant.core.handler;
import io.netty.channel.SimpleChannelInboundHandler;

import com.seeplant.model.WatcherProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * 状态监控Handler
 * @author yuantao
 *
 */

public class SystemWatcherHandler  extends SimpleChannelInboundHandler<String>{
    private StringBuffer sb = new StringBuffer();
    
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        //开启定时任务
//        ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                // 定时上报在线人数
//            }
//        }, 1, 3, TimeUnit.MINUTES);
//    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // TODO Auto-generated method stub
        sb.append(msg.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        String result = sb.toString();
        sb.setLength(0); //清空
        WatcherProcessor processor = new WatcherProcessor(result);
        result = processor.run();
        ctx.writeAndFlush(result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        ctx.writeAndFlush(cause.getMessage());
        ctx.close(); //出异常的时候关闭channel
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("调试关闭");
    }
}
