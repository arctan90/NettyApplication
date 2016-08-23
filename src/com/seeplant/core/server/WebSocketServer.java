 package com.seeplant.core.server;

import java.net.InetSocketAddress;

import com.seeplant.core.handler.WebSocketFrameHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Websocket 连接的服务
 * @author yuantao
 *
 */
public class WebSocketServer implements Runnable{
    private int port;
//    private static final String WEBSOCKET_PATH = Property.getProperty("WEBSOCKET_PATH");
    
    public WebSocketServer(int port) {
        this.port = port;
    }
    /**
     * 1. 事件循环EventLoopGroup
     * 2. ServerBootstrap作为事件循环线程的容器
     * @throws InterruptedException
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

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossLoop, workerLoop)                
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 默认采用AdaptiveRecvByteBufAllocator分配器，不需要配置
                //.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_SNDBUF, 1024*256) //发包缓冲区，单位多少？
                .option(ChannelOption.SO_RCVBUF, 1024*256) //收包换成区，单位多少？
                .option(ChannelOption.TCP_NODELAY, true); //TCP立即发包
            if (osName.equals("Linux")) {
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
                        //websocket这里不能设置这个玩意
                        ch.pipeline().addLast(new ReadTimeoutHandler(45)); //长时间不写会断
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        ch.pipeline().addLast(new HttpObjectAggregator(65535));
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("", null, true));
//                        ch.pipeline().addLast(new WebSocketIndexPageHandler(""));
                        ch.pipeline().addLast(new WebSocketFrameHandler());
                        
                    }
                });
            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()){
                System.out.println("Web Socket server on at port: " + port + ".");
            }
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            
        } finally {
            workerLoop.shutdownGracefully();
            bossLoop.shutdownGracefully();
        }
    }
}
