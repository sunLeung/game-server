package common.net;

import common.boot.GameServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class HttpServer {
	private static Logger logger= LoggerFactory.getLogger(HttpServer.class.getName());
	private static ChannelFuture future;
	private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);;
	private static EventLoopGroup workerGroup = new NioEventLoopGroup();;
	private static DefaultEventExecutorGroup executorGroup = new DefaultEventExecutorGroup(10);
	
	public static void startHttpServer(final int port){
			try {
			    ServerBootstrap b = new ServerBootstrap();
			    b.group(bossGroup, workerGroup)
				    .channel(NioServerSocketChannel.class)
				    .childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
						    ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
						    ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
						    ch.pipeline().addLast("packet-decoder",new HttpPacketDecoder());
						    ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
						    if(GameServer.isTrace)
						    	ch.pipeline().addLast("packet-encoder",new HttpPacketEncoder());
						    ch.pipeline().addLast(executorGroup,"HttpServerHandler",new HttpServerHandler());
//						    ch.pipeline().addLast("HttpServerHandler",new HttpServerHandler());
						}
				    });
			future = b.bind(new InetSocketAddress(port)).sync();
			logger.info("HTTP server started.Listening:" + port);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	 
	public static void stopHttpServer(){
		try {
			if(future!=null)
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
