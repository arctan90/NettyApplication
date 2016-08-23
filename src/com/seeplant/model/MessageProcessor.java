package com.seeplant.model;

import com.seeplant.protobuf.Bye;
import com.seeplant.protobuf.Login;
import com.seeplant.protobuf.Ping;
import com.seeplant.protobuf.Protocol;
import com.seeplant.util.MyLogger;
import com.seeplant.util.WriteWrapper;

import io.netty.channel.ChannelHandlerContext;

/**
 * socket 链接的消息处理入口
 * 包括：
 *      老师端构建群组
 *      老师端接收主观题答案
 *      老师端接收客观题答案
 *      老师端结束课堂
 *      学生端上传笔迹
 *      学生完成答疑的通知
 *      
 *      
 * @author yuantao
 *
 */
public class MessageProcessor implements Runnable{
    private Protocol protocol = null;
    private ChannelHandlerContext ctx = null;
    public MessageProcessor(Protocol protocol, ChannelHandlerContext ctx){
        this.protocol = protocol;
        this.ctx = ctx;
    }
    
    @Override
    public void run() {
        if (protocol.hasPing()){
            processPing(protocol.getPing());
        } else if (protocol.hasBye()) {
            processBye(protocol.getBye());
        } else {
            MyLogger.warning("our building is under attack.");
        }
    }

    /**
     * 
     * @param login
     */
    public int processLogin(Login login) {
        int responseCode = 0;
        return responseCode;
    }
    
    private void processPing(Ping ping){
        WriteWrapper.write(ctx, ping);
    }
    
    private void processBye(Bye bye) {

    }
}
