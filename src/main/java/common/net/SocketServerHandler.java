package common.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SocketServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		ByteBuf bb = (ByteBuf) msg;
		int length = bb.readInt();
		System.out.println(length);
		int i = bb.readInt();
		System.out.println(i);
		int j = bb.readInt();
		byte[] strb=new byte[j];
		bb.readBytes(strb);
		System.out.println(new String(strb));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
