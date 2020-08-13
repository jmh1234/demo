package com.demo;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class ForkJoin {
    @Test
    public void testOne() {
        long sum = 0L;
        Instant start = Instant.now();
        for (long i = 0; i <= 100000000000L; i++) {
            sum += i;
        }

        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println("耗费时间为：" + Duration.between(start, end).toMillis()); //27715
    }

    /**
     * ForkJoin框架
     */
    @Test
    public void testTwo() {
        Instant start = Instant.now();
        ForkJoinPool pool = new ForkJoinPool();
        // ForkJoinTask代表一个可以并行、合并的任务。ForkJoinTask是一个抽象类
        // 它有两个抽象子类：RecursiveAction和RecursiveTask
        ForkJoinTask<Long> task = new com.demo.example.forkJoin.ForkJoin(0, 100000000000L);
        Long sum = pool.invoke(task);

        System.out.println(sum);
        Instant end = Instant.now();
        //如果数值过小，拆分需要时间，效率就会比for循环还要低
        System.out.println("耗费时间为：" + Duration.between(start, end).toMillis());
    }

    @Test
    public void testThree() {
        Instant start = Instant.now();
        //底层就是forkjoin
        long sum = LongStream.rangeClosed(0, 100000000000L)
                .parallel()  // 并行流，若顺序流则为sequential()
                .reduce(0, Long::sum);
        Instant end = Instant.now();
        System.out.println(sum);
        System.out.println("耗费时间为：" + Duration.between(start, end).toMillis());
    }
}
