package com.demo.example.netty;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * NettyPromise测试
 *
 * @author Ji MingHao
 * @since 2022-05-17 09:45
 */
@Log4j2
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final EventLoop eventLoop = new NioEventLoopGroup().next();
        final DefaultPromise<Object> promise = new DefaultPromise<>(eventLoop);
        Runnable runnable = () -> {
            log.info("开始计算...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
            promise.setSuccess(80);
        };
        final Thread thread = new Thread(runnable);
        thread.start();
        log.info("等待结果...");
        log.info("执行的结果：{}", promise.get());
    }
}
