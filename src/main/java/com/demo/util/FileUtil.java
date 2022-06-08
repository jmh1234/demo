package com.demo.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 文件工具类
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
public class FileUtil {

    private FileUtil() {

    }

    private static final int BUFFER_SIZE = 1024;
    private static final Logger logger = LoggerUtil.getInstance(FileUtil.class);

    /**
     * 普通的NIO读取文件
     *
     * @param filePath 文件路径
     * @return 文件内容
     */
    public static String readToString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

    /**
     * 通过 FileChannel.map()拿到MappedByteBuffer
     * 使用内存文件映射，速度会快很多
     *
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException IOException
     */
    public static String readByChannel(Path filePath) throws IOException {
        final File file = filePath.toFile();
        StringBuilder sb = new StringBuilder();
        try (FileChannel channel = new RandomAccessFile(file, "rw").getChannel()) {
            long size = channel.size();
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            if (size > BUFFER_SIZE) {
                byte[] bytes = new byte[BUFFER_SIZE];
                long cycle = size / BUFFER_SIZE;
                int mode = (int) (size % BUFFER_SIZE);
                for (int i = 0; i < cycle; i++) {
                    mappedByteBuffer.get(bytes);
                    sb.append(new String(bytes));
                }
                if (mode > 0) {
                    bytes = new byte[mode];
                    mappedByteBuffer.get(bytes);
                    sb.append(new String(bytes));
                }
            } else {
                byte[] all = new byte[(int) size];
                mappedByteBuffer.get(all, 0, (int) size);
                sb.append(new String(all));
            }
        }
        return sb.toString();
    }

    public static List<String> getFile(String path) {
        List<String> fileNameList = new ArrayList<>();
        File originFile = new File(path);
        File[] files = originFile.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        for (File file : files) {
            if (file.isFile()) {
                fileNameList.add(file.getName());
            } else if (file.isDirectory()) {
                getFile(file.getPath());
            }
        }
        return fileNameList;
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     *
     */
    public static void delAllFile(String path) {
        String fileDeleteMsg = "文件删除成功";
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            if (file.mkdirs()) {
                logger.warn("{}文件不存在，已创建！", path);
            }
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp;
        if (tempList == null) {
            return;
        }
        for (String tempFile : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempFile);
            } else {
                temp = new File(path + File.separator + tempFile);
            }

            boolean file1 = temp.isFile();
            boolean delete = temp.delete();
            if (path.endsWith("sourcecode")) {
                if (file1 && !tempFile.endsWith(".txt") && !tempFile.endsWith(".TXT") && delete) {
                    logger.info(fileDeleteMsg);
                }
            } else if (path.contains("out\\padding")) {
                if (file1 && !tempFile.endsWith(".bat") && !tempFile.endsWith(".BAT") && delete) {
                    logger.info(fileDeleteMsg);
                }
            } else {
                if (file1 && delete) {
                    logger.info(fileDeleteMsg);
                }
            }
            if (temp.isDirectory()) {
                // 先删除文件夹里面的文件
                delAllFile(path + "/" + tempFile);
                // 再删除空文件夹
                delFolder(path + "/" + tempFile);
            }
        }
    }

    /***
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径
     */
    private static void delFolder(String folderPath) {
        delAllFile(folderPath);
        File myFilePath = new File(folderPath);
        if (myFilePath.delete()) {
            logger.info("文件删除成功");
        }
    }
}
