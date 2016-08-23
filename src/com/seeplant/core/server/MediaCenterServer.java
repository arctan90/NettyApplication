 package com.seeplant.core.server;

import java.net.InetSocketAddress;

import com.seeplant.core.handler.SystemWatcherHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 这个类作为程序的状态查询后台
 * @author yuantao
 */
public class MediaCenterServer implements Runnable{
    private int port;
    
    public MediaCenterServer(int port) {
        this.port = port;
    }
    /**
     * 1. 事件循环EventLoopGroup
     * 2. ServerBootstrap作为事件循环线程的容器
     */
    public void run(){
        String osName = System.getProperty("os.name");
        
        EventLoopGroup bossLoop = null;
        EventLoopGroup workerLoop = null;
        if (osName.equals("Linux")) {
           bossLoop = new EpollEventLoopGroup();
           workerLoop = new EpollEventLoopGroup();
        } else {
          bossLoop = new NioEventLoopGroup();
          workerLoop = new NioEventLoopGroup();
        }

        String delimiter = "\r\n";

        ByteBuf[] delimiterBytes = new ByteBuf[] {
                Unpooled.wrappedBuffer(delimiter.getBytes()) };
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossLoop, workerLoop)
                .option(ChannelOption.TCP_NODELAY, true); //TCP立即发包
            if (osName.equals("Linux")) { //Linux平台用Epoll模式
                bootstrap.channel(EpollServerSocketChannel.class);
             } else {
                 bootstrap.channel(NioServerSocketChannel.class);
             }
             bootstrap.localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // TODO Auto-generated method stub
                        ch.pipeline().addLast("logging", new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder( 
                                64, delimiterBytes));
                        ch.pipeline().addLast(new StringDecoder()); 
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new SystemWatcherHandler());
                    }
                });
            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()){
                System.out.println("Monitor server on at port: " + port + ".");
            }
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            
        } finally {
            for (int i=0; i<delimiterBytes.length;i++){
                delimiterBytes[i].release();
            }
            workerLoop.shutdownGracefully();
            bossLoop.shutdownGracefully();
        }
    }
}
