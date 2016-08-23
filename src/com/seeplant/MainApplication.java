package com.seeplant;

import com.seeplant.core.server.MediaCenterServer;
import com.seeplant.core.server.ProtoBufMediaCenterServer;
import com.seeplant.core.server.WebSocketServer;
import com.seeplant.util.AssistantList;
import com.seeplant.util.ChatFilter;
import com.seeplant.util.MyLogger;
import com.seeplant.util.Property;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

public class MainApplication {  
    static {
        ChatFilter.initChatFilter();
        AssistantList.init();
    }

    public static void main(String[] argv) throws Exception{
        MyLogger.log("雷猴");

        String str = Property.getProperty("io.netty.leakDetection.level");
        System.setProperty("io.netty.leakDetection.level", str);

        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
        threads();
    }

    private static void threads() {
      new Thread(new WebSocketServer(
              new Integer(Property.getProperty("WebSocketPort")).intValue())).start();
      new Thread(new ProtoBufMediaCenterServer(
              new Integer(Property.getProperty("MediaServerPort")).intValue())).start();
      new Thread(new MediaCenterServer(12345)).start();
    }
}
