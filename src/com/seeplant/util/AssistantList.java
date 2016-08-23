package com.seeplant.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class AssistantList {
    private final static String fileName = "assistant.list";
    private static HashSet<Long> set = new HashSet<>();
    static {
        File file = new File(System.getProperty("user.dir") + fileName);
        if (!file.exists())
        {
            file = new File(Property.class.getResource("/").getPath() + fileName);
        }
         
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));){
            String line = null;
            
            while ((line = bReader.readLine()) != null) {
                set.add(Long.valueOf(line));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
    
    public static void init() { 
    }
    
    public static boolean isAssistant(Long uid) {
        return set.contains(uid);
    }
}
