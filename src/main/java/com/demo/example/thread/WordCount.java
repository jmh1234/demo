package com.demo.example.thread;

import com.demo.constant.ProgramConstant;
import com.demo.util.FileUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * WordCount
 *
 * @author Ji MingHao
 * @since 2022-04-29 09:09
 */
public class WordCount {

    private final int num;
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("mpsms-backup-pool-%d").build();

    private WordCount(int threadNum) {
        num = threadNum;
    }

    private Map<String, Integer> count(File file) throws IOException, ExecutionException, InterruptedException {
        Map<String, Integer> finalResult = new HashMap<>(ProgramConstant.MAP_INIT_CAPACITY);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(num, num, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        try ( BufferedReader reader = new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < num; i++) {
                Future<Map<String, Integer>> singleResult = executor.submit(() -> {
                    String line;
                    Map<String, Integer> result = new HashMap<>(ProgramConstant.MAP_INIT_CAPACITY);
                    while ((line = reader.readLine()) != null) {
                        String[] words = line.split(" ");
                        for (String word : words) {
                            result.put(word, result.getOrDefault(word, 0) + 1);
                        }
                    }
                    return result;
                });
                Map<String, Integer> lineMapData = singleResult.get();
                for (Map.Entry<String, Integer> entry : lineMapData.entrySet()) {
                    finalResult.put(entry.getKey(), finalResult.getOrDefault(entry.getKey(), 0) + entry.getValue());
                }
            }
        }
        executor.shutdown();
        return finalResult;
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String filePath = "/src/main/java/com/demo/example/thread/test.txt";
        String path = System.getProperty("basedir", System.getProperty("user.dir")) + filePath;
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        String content = FileUtil.readToString(path);
        if (content != null) {
            String[] split = content.split(System.lineSeparator());
            System.out.println(Arrays.asList(split));
        }
        WordCount wordCount = new WordCount(10);
        Map<String, Integer> count = wordCount.count(file);
        System.out.println(count);
    }
}
