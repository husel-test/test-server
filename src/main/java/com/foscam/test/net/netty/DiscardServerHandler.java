package com.foscam.test.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends SimpleChannelInboundHandler<Object>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		ByteBuf in = (ByteBuf)msg;
		
		try {
			
			while(in.isReadable()){
				System.out.print((char)in.readByte());
				System.out.flush();
			}
			//System.out.println(buf.toString(CharsetUtil.US_ASCII));
			System.out.println(".....1");
			in.release();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			//ReferenceCountUtil.release(msg);
		}
		
		
		
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		//super.exceptionCaught(ctx, cause);
		
		cause.printStackTrace();
		
		ctx.close();
		System.out.println("......2");
	}

}
