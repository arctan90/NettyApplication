package com.seeplant.model;

import com.seeplant.util.Version;

public class WatcherProcessor {

    
    private static final String help = "帮助文档：\n";
    
    private String mString = "";
    public WatcherProcessor(String mString) {
        // TODO Auto-generated constructor stub
        this.mString = mString;
    }
    
    public String run() {
        String result = "";
        if (mString.isEmpty()){
            return result;
        }else if (mString.equals("version")) {
            result = Version.getInfo();
        } else if (mString.equals("usage") || mString.equals("help")) {
            result = help;
        } else if (mString.equals("list")) {
            
        }
        return result;
    }
}
