package com.seeplant.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 聊天过滤器，DFA算法
 * @author yuantao
 *
 */
public class ChatFilter {
    private static HashMap<String, ChatFilterTreeNode> sensitiveMap = new HashMap<>();
    
    static {
        File file = new File(ChatFilter.class.getResource("/").getPath()+"NoneWantToSee.list");
        TreeSet<String> set = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // TODO Auto-generated method stub
                return o1.length() > o2.length() ? -1 : 1;
            }
        });
        try(BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
            String line = null;
            
            while ((line = bReader.readLine()) != null) {
                set.add(line);
            }
            initFilter(set);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    /**
     * 只是用来加载静态代码的
     */
    public static void initChatFilter(){}
    /**
     * 构造关键词查询器
     * @param keySet 按长度倒叙排列的TreeSet
     */
    private static void initFilter(TreeSet<String> keySet) { 
        for (String oneKey : keySet) {
            HashMap<String, ChatFilterTreeNode> iterMap = sensitiveMap;
            for (int index = 0; index < oneKey.length(); ++index) {
                char keyChar = oneKey.charAt(index);
                ChatFilterTreeNode node = iterMap.get(String.valueOf(keyChar)); // 按一个字符查找 
                if (node != null) { //如果存在尝试下探
                    if (index < (oneKey.length()-1)) {
                        node.setEnd(false);
                        node.setOverLapEnd(false);
                    } else { //部分匹配
                        if (!node.getNextNodeMap().isEmpty()) {
                            node.setEnd(false);
                            node.setOverLapEnd(true);
                        }
                    }
                    iterMap = node.getNextNodeMap();
                } else {
                    //不存在就构造
                    ChatFilterTreeNode nextNewNode = new ChatFilterTreeNode();
                    if (index < (oneKey.length()-1)) {
                        nextNewNode.setEnd(false);
                    }
                    iterMap.put(String.valueOf(keyChar), nextNewNode);
                    iterMap = nextNewNode.getNextNodeMap();
                }
            }
        }
    }
    
    
    public String filte(String targetStr, Long uid, String userType) {
        HashMap<String, ChatFilterTreeNode> iterMap = sensitiveMap;
        StringBuilder sb = new StringBuilder();
        boolean isLeagle = true;
        boolean needProcessOverlap = false;
        int sensitivityIndex = 0; // 标记敏感词起始位置
        int normalStartIndex = 0; // 标记正常词起始位置
        int normalEndIndex = 0; // 标记正常词结束位置 
        for (int index = 0; index < targetStr.length(); index++) {
            char inputChar = targetStr.charAt(index);
            ChatFilterTreeNode node = iterMap.get(String.valueOf(inputChar));
            if (node != null) {
                iterMap = node.getNextNodeMap();
                if (node.isEnd()) {
                    //匹配上了先替换敏感词再调整索引值
                    if (normalEndIndex > normalStartIndex) { //先截取前面的非敏感词部分
                        sb.append(targetStr.substring(normalStartIndex, normalEndIndex));
                    }
                    sb.append("**");
                    
                    normalStartIndex = index + 1;
                    sensitivityIndex = index + 1;
                    normalEndIndex = index + 1;
                    isLeagle = false;
                    iterMap = sensitiveMap;
                    needProcessOverlap = false;
                    isLeagle = false;
                } else if (node.isOverLapEnd()) {
                    needProcessOverlap = true;
                    isLeagle = false;
                }
                
            } else { 
                if (needProcessOverlap) { //处理重叠匹配的状态
                    if (normalEndIndex > normalStartIndex) {
                        sb.append(targetStr.substring(normalStartIndex, normalEndIndex));
                    }
                    sb.append("**");
                    needProcessOverlap = false;
                    normalStartIndex = index;
                    sensitivityIndex = index;
                    normalEndIndex = index;
                }
                //这里要尝试去匹配一下, 如果匹配了一半退出了，需要重新去匹配
                iterMap = sensitiveMap;
                node = iterMap.get(String.valueOf(inputChar));
                if (node != null) {
                    normalEndIndex = index;
                    sensitivityIndex = index;
                    iterMap = node.getNextNodeMap();
                    if (node.isEnd()) {
                        //匹配上了先替换敏感词再调整索引值
                        if (normalEndIndex > normalStartIndex) { //先截取前面的非敏感词部分
                            sb.append(targetStr.substring(normalStartIndex, normalEndIndex));
                        }
                        sb.append("**");
                        
                        normalStartIndex = index + 1;
                        sensitivityIndex = index + 1;
                        normalEndIndex = index + 1;
                        isLeagle = false;
                        iterMap = sensitiveMap;
                    }
                    
                } else {
                    //1.正常start=正常end=敏感start，让正常end=index，
                    if (normalEndIndex == normalStartIndex 
                            && normalEndIndex == sensitivityIndex) {
                        iterMap = sensitiveMap;
                        sensitivityIndex = normalStartIndex;
                    }
                    normalEndIndex = index + 1;
                }
                
            }
        }
        if (needProcessOverlap) {
            if (normalStartIndex < normalEndIndex) {
                sb.append(targetStr.substring(normalStartIndex, normalEndIndex));
            }
            sb.append("**");
            normalStartIndex = targetStr.length();
            sensitivityIndex = targetStr.length();
            normalEndIndex = targetStr.length();
        }
        if (normalStartIndex < targetStr.length()) {
            sb.append(targetStr.substring(normalStartIndex));
        }
        if (!isLeagle) {
            StringBuilder theSb = new StringBuilder().append(userType).append(" [uid:").append(uid.toString())
                    .append("] 不合适的言论:").append(targetStr);
           MyLogger.log(theSb.toString());
        }
        return filteHtml(sb.toString());
    }
    
    private String filteHtml(String string) {
        StringBuffer sb = new StringBuffer();
        String rex = "(?!<img)\\s+(?=.*title=\"\\S+\")(?=.*class=\"emoji\\s+emoji\\w+\")(?=.*src=\"/static/img/spacer.3254726.gif\")(?!>)";

        Pattern pattern = Pattern.compile(rex, Pattern.CANON_EQ);
        Matcher matcher = pattern.matcher(string);
        ArrayList<String> emoji = new ArrayList<>();
        int start = 0;
        while(matcher.find()) {
            emoji.add(matcher.group(matcher.groupCount()));
            String subString = string.substring(start, matcher.start());
            sb.append(strimHtml(subString)); 
            start = matcher.end();
            sb.append(matcher.group(matcher.groupCount()));
        }
        
        if (start < string.length()) {
            String subString = string.substring(start, string.length());
            sb.append(strimHtml(subString));
        }
        return sb.toString(); 
    }

    private String strimHtml(String input) {
        return input.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("<[^>]*>", "");
    }
}
