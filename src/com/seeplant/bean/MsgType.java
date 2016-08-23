package com.seeplant.bean;

public class MsgType {
    public final static String HTTP_LOGIN = "LOGIN";
    public final static String HTTP_BYE = "BYE";
    
    public final static String WEBSOCKET_LOGIN = "login";
    public final static String WEBSOCKET_BYE = "chatBye";
    public final static String WEBSOCKET_QUESTION = "question";
    public final static String WEBSOCKET_Q_ANSWER = "qAnswer";
    public final static String WEBSOCKET_Q_END = "qEnd";
    public final static String WEBSOCKET_NOTICE = "notice";
    public final static String WEBSOCKET_QUERY_ANSWER = "queryAnswer";
    /**
     * 禁言
     */
    public final static String WEBSOCKET_COUNTERSPELL = "curse";
    public final static String WEBSOCKET_CURSESTATUS = "curseStatus";
    public final static String WEBSOCKET_SPELL = "chat";
    
    public final static String WEBSOCKET_LIBRA = "libra";
}
