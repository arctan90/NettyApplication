package com.seeplant.util;


public class Version {
    private static final String version = "20160811 版本1.3.1.1625\n";
    private static final String author = "\n";
    private static final String company = "\n";
    private static final String resentlyModified = "最近更新内容"
            + "\n";

    public static String getInfo() {
        return version + author + company + resentlyModified;
    }
}
