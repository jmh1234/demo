package com.demo.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * 使用netty实现客户端
 *
 * @author Ji MingHao
 * @since 2022-05-16 09:13
 */
@Log4j2
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        final NioEventLoopGroup group = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) {
                channel.pipeline().addLast(new StringEncoder());
                channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        final ByteBuf buffer = ctx.alloc().buffer(16);
                        buffer.writeBytes(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                        ctx.writeAndFlush(buffer);
                    }
                });
            }
        });
        final ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 8080));
        final Channel channel = channelFuture.sync().channel();

        Runnable runnable = () -> {
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    final String line = scanner.next();
                    if ("q".equals(line)) {
                        channel.close();
                        break;
                    } else {
                        channel.writeAndFlush(line);
                    }
                }
            }
        };
        final Thread input = new Thread(runnable, "input");
        input.start();

        channel.closeFuture()
                .addListener((ChannelFutureListener) channelFuture1 -> {
                    log.info("关闭之后...");
                    group.shutdownGracefully();
                });
    }
}
