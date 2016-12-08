package com.seeplant.util;


public class Version {
    private static final String version = "20160811 版本1.0.1.16.1206.1514\n";
    private static final String author = "\n";
    private static final String company = "\n";
    private static final String resentlyModified = "最近更新内容"
            + "增加了删除特征的操作\n";

    public static String getInfo() {
        return version + author + company + resentlyModified;
    }
}
