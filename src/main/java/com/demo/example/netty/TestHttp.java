package com.demo.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;

import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;

/**
 * Created with IntelliJ IDEA.
 * Http请求测试
 *
 * @author Ji MingHao
 * @since 2022-05-17 15:29
 */
@Log4j2
public class TestHttp {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) {
                    channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    channel.pipeline().addLast(new HttpServerCodec());
                    channel.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) {
                            log.info(httpRequest.uri());
                            final DefaultFullHttpResponse response =
                                    new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
                            final byte[] bytes = "<h1>Hello, world1<h1>".getBytes();
                            response.content().writeBytes(bytes);
                            response.headers().setInt(CONTENT_LENGTH, bytes.length);
                            ctx.writeAndFlush(response);
                        }
                    });
                }
            });
            final ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(8080)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
