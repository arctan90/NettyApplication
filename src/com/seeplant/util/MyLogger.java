package com.seeplant.util;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class MyLogger {
    private static final InternalLogger log =InternalLoggerFactory.getInstance(MyLogger.class);
    private static final boolean enableDebug;
    private static boolean enableDebugLogger;
    static {
        String mode = Property.getProperty("debugModel");
        if (mode.equals("debug")) {
            enableDebug = true;
        } else {
            enableDebug = false;
        }
        
        mode = Property.getProperty("debugLogEnable");
        if (mode.equals("true")) {
            enableDebugLogger = true;
        } else {
            enableDebugLogger = false;
        }
    }
    public static void log(String msg) {
        msg = "[APP]["+System.currentTimeMillis() + "]: " + msg;
        if (enableDebug){
            System.out.println(msg);
        } else {
            log.info(msg);
        }
    }
    
    public static void warning(String msg) {
        msg = "[APP]["+System.currentTimeMillis() + "]: " + msg;
        if (enableDebug){
            System.out.println(msg);
        } else {
            log.warn(msg);
        }
    }
    
    public static void debug(String msg) {
        if (enableDebugLogger) {
            log.info(msg);
        }
    }
    
    public static void setEnableDebugLogger(boolean msg) {
        enableDebugLogger = msg;
    }
    
    public static boolean getEnableDebugLogger() {
        return enableDebugLogger;
    }
}
