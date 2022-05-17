package com.demo.example.netty;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * NettyFuture 测试
 *
 * @author Ji MingHao
 * @since 2022-05-17 09:37
 */
@Log4j2
public class TestNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        final EventLoop eventLoop = group.next();
        final Future<Integer> future = eventLoop.submit(() -> {
            Thread.sleep(1000);
            return 50;
        });
        log.info("等待结果...");
        log.info("接受执行结果：{}", future.get());
        future.addListener(future1 -> log.info("接受执行结果：{}", future1.getNow()));
    }
}
