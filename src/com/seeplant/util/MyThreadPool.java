package com.seeplant.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPool {
    private static int poolLength = Runtime.getRuntime().availableProcessors();
    private static ExecutorService pool = Executors.newFixedThreadPool(poolLength);
    
    private MyThreadPool(){}
    
    synchronized public static void run(Runnable task){
        pool.submit(task);
    }
    
    public static void shutdown(){
        pool.shutdown();
    }
    /**
     * 用来加载该单例
     */
    public static void init(){}
}
