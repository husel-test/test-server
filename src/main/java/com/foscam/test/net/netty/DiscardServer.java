package com.foscam.test.net.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

	public static void main(String[] args) {
		
		
		new DiscardServer().run();

	}
	
	public  void run(){
		
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			
			bootstrap.group(boss, worker)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					
					channel.pipeline().addLast(new DiscardServerHandler());
					
				}
			})
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = bootstrap.bind(8080).sync();
			f.channel().closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
		
		
	}
}
