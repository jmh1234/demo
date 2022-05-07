package com.demo.aspect;

import com.demo.annotation.Cache;
import com.demo.annotation.Log;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * 装饰器服务层
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
public class DecoratorService implements LogProxyInterface {

    private static final Random RANDOM = new Random();

    @Cache(cacheSeconds = 2)
    public List<Object> queryDataWithCache(int id) {
        return queryData(id);
    }

    public List<Object> queryDataWithoutCache(int id) {
        return queryData(id);
    }

    public static List<Object> queryData(int id) {
        // 模拟一个查询操作
        int size = RANDOM.nextInt(id) + 10;
        return IntStream.range(0, size)
                .mapToObj(i -> RANDOM.nextInt(id))
                .collect(Collectors.toList());
    }

    @Log
    @Override
    public void addLogByProxy(String logContent) {
        System.out.println("the method is invoke by " + logContent);
    }
}
