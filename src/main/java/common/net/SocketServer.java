package common.net;

import common.boot.GameServer;
import common.log.Logger;
import common.log.LoggerManger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.InetSocketAddress;

public class SocketServer {
    private static Logger logger = LoggerManger.getLogger();
    private static ChannelFuture future;
    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();
    private static DefaultEventExecutorGroup executorGroup = new DefaultEventExecutorGroup(10);

    public static void startSocketServer(final int port) {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("frame-decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
                            ch.pipeline().addLast("work-handler", new SocketServerHandler());
                        }
                    })
                    //三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //TCP层面心跳
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            future = b.bind(port).sync();
            logger.info("Socket server started.Listening:" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopSocketServer() {
        try {
            if (future != null)
                future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bossGroup != null)
                bossGroup.shutdownGracefully();
            if (workerGroup != null)
                workerGroup.shutdownGracefully();
            if (executorGroup != null)
                executorGroup.shutdownGracefully();
        }

    }
}
