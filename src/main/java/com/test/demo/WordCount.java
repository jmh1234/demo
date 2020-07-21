package com.test.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class WordCount {
    private final int num;
    private static ExecutorService threadPool;

    private WordCount(int threadNum) {
        num = threadNum;
        threadPool = Executors.newFixedThreadPool(threadNum);
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String path = "C:\\Users\\86158\\Desktop\\1571383015316\\test.txt";
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("file is not exit");
            return;
        }
        WordCount wordCount = new WordCount(10);
        Map<String, Integer> count = wordCount.Count(file);
        System.out.println(count);
        threadPool.shutdown();
    }

    private Map<String, Integer> Count(File file) throws IOException, ExecutionException, InterruptedException {
        // 读取文件
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        // 任务结束返回结果的集合
        List<Future<Map<String, Integer>>> totalResult = new ArrayList<>();
        // 最后返回的集合
        Map<String, Integer> finalResult = new HashMap<>();
        for (int i = 0; i < num; i++) {
            final int id = i;
            // 一个线程读取一行文字
            Future<Map<String, Integer>> singleResult = threadPool.submit(new Callable<Map<String, Integer>>() {
                @Override
                public Map<String, Integer> call() throws Exception {
                    String line;
                    Map<String, Integer> result = new HashMap<>();
                    while ((line = reader.readLine()) != null) {
                        // 每一行以空格分隔
                        System.out.println(Thread.currentThread().getName() + " ：" + id + " ：" + line);
                        String[] words = line.split(" ");
                        // 把每个单词存到map里
                        for (String word : words) {
                            if (word.matches("[/(^bai[\\-0-9][0-9]]")) {
                                continue;
                            }
                            result.put(word, result.getOrDefault(word, 0) + 1);
                        }
                    }
                    return result;
                }
            });
            totalResult.add(singleResult);
        }

        for (Future<Map<String, Integer>> future : totalResult) {
            // 把每一个future对象里的map集合合并到 finalResult
            Map<String, Integer> lineMapData = future.get();
            // 遍历finalResult
            for (Map.Entry<String, Integer> entry : lineMapData.entrySet()) {
                finalResult.put(entry.getKey(), finalResult.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        return finalResult;
    }
}
