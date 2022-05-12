package com.demo.example.netty;

import com.demo.util.FileUtil;

import java.io.File;
import java.io.IOException;

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
        String path = System.getProperty("basedir", System.getProperty("user.dir")) + File.separator + filePath;
        try {
            System.out.println(FileUtil.readByChannel(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
