package com.seeplant.util;

import com.google.protobuf.MessageLiteOrBuilder;

/**
 * 所有的业务模块要用这个对象添加命令字封装，然后用自己的解析
 * @author yuantao
 *
 */
public class MyMessageLite {
    private MessageLiteOrBuilder messageLit = null;
    byte[] command = new byte[2];
    
    public MyMessageLite(byte[] command, MessageLiteOrBuilder messageLit){
        this.command = command;
        this.messageLit = messageLit;
    }
    
    public MessageLiteOrBuilder getMessageLit() {
        return messageLit;
    }
    public void setMessageLit(MessageLiteOrBuilder messageLit) {
        this.messageLit = messageLit;
    }
    public byte[] getCommand() {
        return command;
    }
    public void setCommand(byte[] command) {
        this.command = command;
    }
}
