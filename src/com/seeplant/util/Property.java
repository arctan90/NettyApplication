package com.seeplant.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    private static Properties properties = new Properties();
    private static boolean needAuthority = false;
    static {
        File file = new File(System.getProperty("user.dir")+"setting.properties");
        if (!file.exists())
        {
            file = new File(Property.class.getResource("/").getPath()+"setting.properties");
        }
         
        try (FileInputStream fis = 
                new FileInputStream(file)){
            properties.load(fis);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        
        if (getProperty("authority").equalsIgnoreCase("on")) {
            needAuthority = true;
        }
    }
    
    private Property(){
    }
   
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static boolean needAuthority() {
        return needAuthority;
    }
}
