package com.foscam.test.net.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class DiscardClient{

	
	public static void main(String[] args) {
		
		EventLoopGroup boss = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			
			bootstrap.group(boss)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					
					channel.pipeline().addLast(new DiscardServerHandler());
					
				}
			});
			
			ChannelFuture f = bootstrap.connect("127.0.0.1", 8080);
			f.channel().closeFuture().sync(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
		}
		
		
	}
}
