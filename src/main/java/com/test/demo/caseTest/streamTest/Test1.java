package com.test.demo.caseTest.streamTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Test1 {

    private static final int[] integerArr = {1, 2, 3, 4, 5, 6, 7, 8};
    private static final List<Integer> integerList = Arrays.asList(1, 1, 2, 3, 4, 5, 5, 6, 7, 8, 9, 9);
    private static final List<String> stringList = Arrays.asList("Java 8", "Lambdas", "In", "Action", "Hello", "World");

    public static void main(String[] args) {
        // 流的获取方式
        Stream<Integer> stream1 = stringList.stream().map(String::length);
        IntStream stream2 = Arrays.stream(integerArr);
        Stream<List<String>> stream3 = Stream.of(Test1.stringList);

        // 通过函数获取流
        Stream<Integer> stream4 = Stream.iterate(0, n -> n + 2).limit(5);
        Stream<Double> stream5 = Stream.generate(Math::random).limit(5);

        // Stream 过滤 得到 其中小于三的元素
        System.out.println("--------------handle4Integer-------------------");
        stream1.distinct() //  去重
                .skip(1) // 忽略第一个数字
                .filter(i -> i >= 3) //  过滤小于三的元素
                .limit(6) //  限制最大返回流的个数(返回前6个数字)
                .collect(Collectors.toList())
                .forEach(System.out::println);  //  将Stream转化为list

        // flatMap流转换
        System.out.println("----------------flatMap流转换-----------------");
        List<String> strList = stringList.stream()
                .map(w -> w.split(" ")) // 循环List对每个元素进行操作(以空格分隔每个元素)
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(strList);

        /** 元素匹配 是否存在哪些元素的判断*/
        // allMatch 匹配 "所有"
        System.out.println("----------------元素匹配-----------------");
        if (integerList.stream().allMatch(i -> i > 3)) {
            System.out.println("值都大于3");
        }

        // anyMatch 匹配 "存在"
        if (integerList.stream().anyMatch(i -> i > 3)) {
            System.out.println("存在大于3的值");
        }

        // 等同于 anyMatch
        /*for (Integer i : integerList) {
            if (i > 3) {
                System.out.println("存在大于3的值");
                break;
            }
        }*/

        // noneMatch 匹配 "所有" 都不存在
        if (integerList.stream().noneMatch(i -> i > 3)) {
            System.out.println("值都小于3");
        }

        /** 终端操作*/
        // 统计流中元素个数
        System.out.println("----------------统计流中元素个数-----------------");
        int result = integerList.size();
        System.out.println(result);
        // 等同于
        /*Long result1 = integerList.stream().count();
        Long result2 = integerList.stream().collect(counting());*/

        // 查找
        // findFirst查找第一个
        System.out.println("----------------查找-----------------");
        Optional<Integer> first = integerList.stream().filter(i -> i > 3).findFirst();
        System.out.println(first.get());
        //findAny随机查找一个
        Optional<Integer> result3 = integerList.stream().filter(i -> i > 3).findAny();
        System.out.println(result3.get());

        // 通过reduce进行处理
        System.out.println("---------------通过reduce进行处理------------------");
        // 通过reduce求和
        int sum = integerList.stream().reduce(0, Integer::sum);
        System.out.println(sum);
        int sum1 = getData().stream().map(Dish::getCalories).reduce(0, Integer::sum);
        System.out.println(sum1);

        // 通过reduce获取最小最大值
        System.out.println("----------------获取流中最小最大值-----------------");
        Optional<Integer> min = getData().stream().map(Dish::getCalories).reduce(Integer::min);
        Optional<Integer> max = getData().stream().map(Dish::getCalories).reduce(Integer::max);
        System.out.println(min.get());
        System.out.println(max.get());

        // 通过summarizingInt同时求总和、平均值、最大值、最小值
        System.out.println("-----------------通过summarizingInt同时求总和、平均值、最大值、最小值----------------");
        IntSummaryStatistics intSummaryStatistics = getData().stream().collect(summarizingInt(Dish::getCalories));
        double average2 = intSummaryStatistics.getAverage();  //获取平均值
        int min2 = intSummaryStatistics.getMin();  //获取最小值
        int max2 = intSummaryStatistics.getMax();  //获取最大值
        long sum2 = intSummaryStatistics.getSum();  //获取总和
        System.out.println(average2);
        System.out.println(min2);
        System.out.println(max2);
        System.out.println(sum2);

        // 通过joining拼接流中的元素
        System.out.println("----------------通过joining拼接流中的元素-----------------");
        String result4 = getData().stream().map(Dish::getName).collect(Collectors.joining(", "));
        System.out.println(result4);

        /** 进阶*/
        // 通过groupingBy进行分组
        System.out.println("----------------通过groupingBy进行分组-----------------");
        Map<String, List<Dish>> dishMap = getData().stream().collect(groupingBy(Dish::getType));
        System.out.println(dishMap);

        // 在collect方法中传入groupingBy进行分组，其中groupingBy的方法参数为分类函数。还可以通过嵌套使用groupingBy进行多级分类
        System.out.println("---------------通过嵌套使用groupingBy进行多级分类------------------");
        Map<String, List<Dish>> result2 = getData().stream().collect(groupingBy(Dish -> {
            if (Dish.getCalories() <= 400) {
                return "400";
            } else if (Dish.getCalories() <= 700) {
                return "700";
            } else {
                return "other";
            }
        }));
        System.out.println(result2);

        // 分区是特殊的分组，它分类依据是true和false，所以返回的结果最多可以分为两组
        System.out.println("----------------分区是特殊的分组-----------------");
        Map<Boolean, List<Dish>> result7 = getData().stream().collect(partitioningBy(Dish::isVegetarian));
        System.out.println(result7);

        // 等同于
        /*Map<Boolean, List<Dish>> result8 = getData().stream().collect(groupingBy(Dish::isVegetarian));
        System.out.println(result8); */
    }

    private static List<Dish> getData() {
        List<Dish> dishList = new ArrayList<>();
        dishList.add(Test.setData("冰淇淋", false, 10, "零食"));
        dishList.add(Test.setData("烧烤", false, 500, "肉类"));
        dishList.add(Test.setData("鸡腿", false, 30, "肉类"));
        dishList.add(Test.setData("青菜", true, 5, "蔬菜"));
        dishList.add(Test.setData("糖", false, 25, "糖类"));
        return dishList;
    }
}
