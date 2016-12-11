package net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class SocketClientHandler extends ChannelInboundHandlerAdapter {

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf bb = ctx.alloc().buffer(4);
        bb.writeInt(9091);
        String abc="fuck";
        bb.writeInt(abc.length());
        bb.writeBytes(new String("abcka").getBytes());
        ctx.writeAndFlush(bb);
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf bb = (ByteBuf) msg;
        int length = bb.readInt();
        System.out.println(length);
        int i = bb.readInt();
        System.out.println(i);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
