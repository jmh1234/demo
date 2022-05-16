package com.demo.example.netty;

import com.demo.util.LoggerUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * 使用netty实现服务端
 *
 * @author Ji MingHao
 * @since 2022-05-16 09:03
 */
public class NettyService {

    private static final Logger logger = LoggerUtil.getInstance(NettyService.class);

    public static void main(String[] args) {
        EventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();
        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        channel.pipeline().addLast(new StringDecoder());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(defaultEventLoopGroup, new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                logger.info("2: {}", msg);
                            }
                        });
                    }
                }).bind(8080);
    }
}
