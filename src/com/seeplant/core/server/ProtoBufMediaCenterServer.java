package com.seeplant.core.server;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import com.seeplant.core.handler.ProtoBufHandler;
import com.seeplant.protobuf.Protocol;
import com.seeplant.util.MyProtobufEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * TCP连接的服务
 * @author yuantao
 *
 */
public class ProtoBufMediaCenterServer implements Runnable{
    private int port;
    public ProtoBufMediaCenterServer(int port) {
        this.port = port;
    }
    
    @Override
    public void run() {
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
                //keepAlive默认是打开
                //.option(ChannelOption.SO_KEEPALIVE, true)
                // 默认采用AdaptiveRecvByteBufAllocator分配器，不需要配置
                //.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                //PooledByteBufAllocator这种分配器是默认分配器，当buffer被写入下一个节点的时候，它会
                //自动释放，并放入pool里面
                //.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_SNDBUF, 1024*256) //发包缓冲区，单位多少？
                .option(ChannelOption.SO_RCVBUF, 1024*256) //收包换成区，单位多少？
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
                        //这里-2是偏移量，指的是从长度帧中取出的数值要减去命令字msg的长度
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
                        pipeline.addLast("readTimeout", new ReadTimeoutHandler(10));
                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(2, -4, true));
                        pipeline.addLast("protoBufEncoder", new MyProtobufEncoder());
                        //pipeline.addLast(new StringEncoder());
                        //包头有2字节包长，2字节类型，类型在本地用反射取出来，不依靠传递的类型
                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 2, 4));
                        pipeline.addLast("protoBufDecoder", new ProtobufDecoder(Protocol.getDefaultInstance())); 
                        pipeline.addLast(new ProtoBufHandler());
                    }
                });
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()){
                System.out.println("ProtoBuf Media server on at "+InetAddress.getLocalHost().getHostAddress()+" port: " + port + ".");
            }
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            
        } finally {
            workerLoop.shutdownGracefully();
            bossLoop.shutdownGracefully();
        }
    }

}
