package com.test.demo.aspect;

import com.test.demo.annotation.Cache;
import com.test.demo.annotation.Log;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecoratorService implements LogProxyInterface {

    @Cache(cacheSeconds = 2)
    public List<Object> queryDataWithCache(int id) {
        return queryData(id);
    }

    public List<Object> queryDataWithoutCache(int id) {
        return queryData(id);
    }

    public static List<Object> queryData(int id) {
        // 模拟一个查询操作
        Random random = new Random();
        int size = random.nextInt(id) + 10;
        return IntStream.range(0, size)
                .mapToObj(i -> random.nextInt(id))
                .collect(Collectors.toList());
    }

    @Log
    @Override
    public String addLogByProxy(String logContent) {
        System.out.println("the method is invoke by " + logContent);
        return String.valueOf(new Random().nextInt());
    }
}
