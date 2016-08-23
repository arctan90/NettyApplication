package com.seeplant.services.HTTPData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.seeplant.myjson.JSONObject;

import com.seeplant.util.MyLogger;
import com.seeplant.util.Property;

/**
 * 一个不咋样的HTTP封装，不是线程序列
 */
public class HttpPost implements Runnable{
    private String address;
    private BufferedWriter writer = null;
    private BufferedReader reader = null;
    private JSONObject jObject = null;
    public HttpPost(String address) {
        this.address = address;
    }
    
    public HttpPost(String address, JSONObject jObject) {
        this.address = address;
        this.jObject = jObject;
    }
    
    public String send(JSONObject jObject) {
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false); //不用缓存
            //设置超时时间
            conn.setConnectTimeout(new Integer(Property.getProperty("connectTimeout")).intValue());
            conn.setReadTimeout(new Integer(Property.getProperty("readTimeout")).intValue());
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "MediaServer");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            writer = new BufferedWriter(new PrintWriter(conn.getOutputStream()));
            writer.write(jObject.toString());
            writer.flush();
            
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            while ((line = reader.readLine()) != null) {
                sb.append(line);    
            }
        } catch(Exception e) {
            MyLogger.log("HttpPost Error:" + e.getMessage());
        } finally {
            try{
                if(writer!=null){
                    writer.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                MyLogger.log("HttpPost Error:" + ex.getMessage());
            }
        }
        MyLogger.log("HTTP Post:"+jObject.toString()+"\nHTTP Response:"+ sb.toString());
       
        return sb.toString();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        this.send(this.jObject);
    }
}
