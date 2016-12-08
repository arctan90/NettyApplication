package com.seeplant.core.server;

import org.seeplant.myjson.JSONObject;

import com.seeplant.core.handler.HTTPHandler;
import com.seeplant.services.HTTPData.HttpPost;
import com.seeplant.util.MyLogger;
import com.seeplant.util.Property;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class HTTPServer implements Runnable{
    private int port;
    static final boolean SSL = System.getProperty("ssl") != null;
    
    public HTTPServer(int port) {
        this.port = port;
    }
    
    public void run() {
        // Configure SSL.
        final SslContext sslCtx;
        EventLoopGroup bossLoop = null;
        EventLoopGroup workerLoop = null;
        try {
            if (SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            String osName = System.getProperty("os.name");
            
            
            if (osName.equals("Linux")) {
               bossLoop = new EpollEventLoopGroup();
               workerLoop = new EpollEventLoopGroup();
            } else {
              bossLoop = new NioEventLoopGroup();
              workerLoop = new NioEventLoopGroup();
            }

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_SNDBUF, 1024*256) //发包缓冲区，单位多少？
                .option(ChannelOption.SO_RCVBUF, 1024*256) //收包换成区，单位多少？
                .option(ChannelOption.TCP_NODELAY, true);
            if (osName.equals("Linux")) { //Linux平台用Epoll模式
                bootstrap.channel(EpollServerSocketChannel.class);
             } else {
                 bootstrap.channel(NioServerSocketChannel.class);
             }
            
            bootstrap.group(bossLoop, workerLoop)
             .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // TODO Auto-generated method stub
                    ChannelPipeline p = ch.pipeline();
                    p.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
                    if (sslCtx != null) {
                        p.addLast(sslCtx.newHandler(ch.alloc()));
                    }
//                    p.addLast(new HttpServerCodec());
                    p.addLast("decoder", new HttpRequestDecoder(8192, 8192, 16384));
                    /**
                     * http-response解码器
                     * http服务器端对response编码
                     */
                    p.addLast("encoder", new HttpResponseEncoder());
                    p.addLast("aggregator", new HttpObjectAggregator(1048576));
                    /**
                     * 压缩
                     * Compresses an HttpMessage and an HttpContent in gzip or deflate encoding
                     * while respecting the "Accept-Encoding" header.
                     * If there is no matching encoding, no compression is done.
                     */
                    p.addLast("deflater", new HttpContentCompressor());
                    
                    p.addLast(new HTTPHandler());
                }
             });

            ChannelFuture future = bootstrap.bind(port).sync();
            
            if (future.isSuccess()){
                System.out.println("HTTP server on at port: " + port + ".");
                //在这里向服务请求加载特征
                String host = Property.getProperty("dataServer") + "/cluster/loadFeature?auth=irisking";
                HttpPost post = new HttpPost(host);
                String response = post.send(new JSONObject());
                MyLogger.log("load Feature response：" + response);
            }
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            bossLoop.shutdownGracefully();
            workerLoop.shutdownGracefully();
        }
    }
}
