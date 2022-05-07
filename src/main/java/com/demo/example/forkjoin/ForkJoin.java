package com.demo.example.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * Created with IntelliJ IDEA.
 * ForkJoin
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class ForkJoin extends RecursiveTask<Long> {

    private static final long serialVersionUID = 1238793729L;
    private final long start;
    private final long end;

    /**
     * 临界值，说明每个小任务最多累加10000个数
     */
    private static final long THRESHOLD = 10000;

    public ForkJoin(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;
        // 如果任务足够小就计算任务
        if (length <= THRESHOLD) {
            long sum = 0;
            //到达临界值进行+操作
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            // 如果任务大于阈值，就分裂成两个子任务计算
            long mid = (start + end) / 2;
            ForkJoin left = new ForkJoin(start, mid);
            ForkJoin right = new ForkJoin(mid + 1, end);
            // 并行执行两个小任务
            left.fork();
            right.fork();
            // 等待任务执行结束合并其结果
            return left.join() + right.join();
        }
    }
}
