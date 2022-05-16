package com.demo.example.netty;

import com.demo.util.LoggerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * 使用netty实现客户端
 *
 * @author Ji MingHao
 * @since 2022-05-16 09:13
 */
public class NettyClient {

    private static final Logger logger = LoggerUtil.getInstance(NettyClient.class);

    public static void main(String[] args) throws InterruptedException {
        final NioEventLoopGroup group = new NioEventLoopGroup();
        final ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        channel.pipeline().addLast(new StringEncoder());
                    }
                }).connect(new InetSocketAddress("localhost", 8080));
        final Channel channel = channelFuture.sync().channel();

        Runnable runnable = () -> {
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    final String line = scanner.next();
                    final boolean interrupted = Thread.currentThread().isInterrupted();
                    if ("q".equals(line) || interrupted) {
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

        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            logger.info("关闭之后...");
            group.shutdownGracefully();
        });
    }
}
