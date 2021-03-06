package common.net;

import common.boot.GameServer;
import common.utils.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Set;

public class HttpPacketDecoder extends MessageToMessageDecoder<FullHttpRequest> {
	private static Logger logger= LoggerFactory.getLogger(HttpPacketDecoder.class.getName());
	@Override
	protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out){
		try {
			if (!msg.decoderResult().isSuccess()) {
				ctx.close();
				return;
			}
			
			String token=msg.headers().get("token");
			String protocolStr=msg.headers().get("protocol");
			String useridStr=msg.headers().get("userid");
			
			logger.debug("Received protocol "+protocolStr);
			
			int protocol=Integer.valueOf(protocolStr.replace("0x", "").trim(),16);
			String data = msg.content().toString(CharsetUtil.UTF_8);
			
			int playerid=-1;
			if(StringUtils.isNotBlank(useridStr))
				playerid=Integer.valueOf(useridStr.trim());
			SocketAddress remoteAddress = ctx.channel().remoteAddress();
			String ip="";
			if(remoteAddress!=null){
				InetSocketAddress inetSocketAddress = (InetSocketAddress) remoteAddress;
				ip=inetSocketAddress.getAddress().getHostAddress();
			}
			HttpPacket packet = new HttpPacket(playerid,token,protocol,data,ip,msg);
			out.add(packet);
			
			if(GameServer.isTrace){
				trace(msg,data);
			}
		} catch (Exception e) {
			ctx.close();
		}
	}
	
	private static void trace(FullHttpRequest msg,String data){
		System.out.println("\n===============request head==================");
		Set<String> set=msg.headers().names();
		for(String s:set){
			System.out.println(s+" : "+msg.headers().get(s));
		}
		System.out.println("==============request head end================\n");
		
		System.out.println("\n===============request body==================");
		System.out.println(data);
		System.out.println("=============request body end=================\n");
	}
}
