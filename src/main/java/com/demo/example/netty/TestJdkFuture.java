package com.demo.example.netty;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * Java JDK Future 测试
 *
 * @author Ji MingHao
 * @since 2022-05-17 09:26
 */
@Log4j2
public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int threadPoolSize = Runtime.getRuntime().availableProcessors();
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("future-test-pool-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        final Future<Integer> future = executor.submit(() -> {
            Thread.sleep(1000);
            return 50;
        });
        log.info("等待结果...");
        log.info("执行的结果：{}", future.get());
    }
}
