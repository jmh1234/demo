package com.demo.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * 使用netty实现服务端
 *
 * @author Ji MingHao
 * @since 2022-05-16 09:03
 */
@Log4j2
public class NettyService {
    public static void main(String[] args) {
        EventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();
        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                final String s = byteBuf.toString(Charset.defaultCharset());
                                super.channelRead(ctx, s);
                            }
                        }).addLast(defaultEventLoopGroup, new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                log.info("1: {}", msg);
                                channel.writeAndFlush(ctx.alloc().buffer().writeBytes("aaaa".getBytes()));
                            }
                        });
                        channel.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                final String s = byteBuf.toString(Charset.defaultCharset());
                                log.info("2: {}", s);
                            }
                        });
                    }
                }).bind(8080);
    }
}
