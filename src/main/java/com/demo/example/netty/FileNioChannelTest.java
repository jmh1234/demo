package com.demo.example.netty;

import com.demo.util.FileUtil;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * NIO文件管道相关测试
 *
 * @author Ji MingHao
 * @since 2022-05-12 15:09
 */
public class FileNioChannelTest {
    public static void main(String[] args) {
        String filePath = "src/main/java/com/demo/example/thread/test.txt";
        try {
            System.out.println(FileUtil.readByChannel(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
