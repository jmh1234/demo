package com.demo.util;

import org.slf4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

    private static final String SUCCESS_MSG = "文件创建成功！";
    private static final int BUFFER_SIZE = 1024;
    private static final Logger logger = LoggerUtil.getInstance(FileUtil.class);

    /**
     * 普通的NIO读取文件
     *
     * @param filePath 文件路径
     * @return 文件内容
     */
    public static String readToString(String filePath) {
        StringBuilder sb = new StringBuilder();
        File file = new File(filePath);
        try (FileChannel channel = new FileInputStream(file).getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            while (true) {
                final int read = channel.read(byteBuffer);
                if (read == -1) {
                    break;
                }
                byteBuffer.flip();
                final byte[] bytes = byteBuffer.array();
                sb.append(new String(bytes));
                byteBuffer.clear();
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
        return sb.toString();
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

    /**
     * 文件拷贝
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static void writeFile(String sourcePath, String targetPath) {
        try (FileChannel from = new FileInputStream(sourcePath).getChannel();
             FileChannel to = new FileOutputStream(targetPath).getChannel()) {
            final long size = from.size();
            long left = size;
            while (left > 0) {
                left -= from.transferTo(size - left, left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加写入文件
     *
     * @param outPath 文件输出路径
     * @param content 文件内容
     * @param append  是否追加写入文件
     */
    public static void writeFile(String outPath, String content, boolean append) {
        File f = new File(outPath);
        try (FileWriter fw = new FileWriter(f, append);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pfp = new PrintWriter(bw)) {
            if (!f.exists()) {
                boolean newFile = f.createNewFile();
                if (newFile) {
                    logger.info(SUCCESS_MSG);
                }
            }
            pfp.println(content);
        } catch (IOException e) {
            logger.error(LoggerUtil.handleException(e));
        }
    }

    /**
     * 根据编码写入文件
     *
     * @param path     文件输出路径
     * @param content  文件内容
     * @param encoding 文档编码
     */
    public static void writeFile(String path, String content, String encoding) {
        File file = new File(path);
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path), encoding)) {
            if (!file.exists()) {
                file = new File(file.getParent());
                if (!file.exists()) {
                    boolean mkdirs = file.mkdirs();
                    if (mkdirs) {
                        logger.info(SUCCESS_MSG);
                    }
                }
            }
            osw.write(content);
            osw.flush();
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
    }

    /**
     * JSON格式化
     *
     * @param jsonStr json字符串
     * @return 格式化后的字符串
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last;
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    public static void toZip(String srcDir, OutputStream out, String type) {
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), type);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name, String type) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            try (FileInputStream in = new FileInputStream(sourceFile)) {
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            }
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if ("export".equals(type)) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if ("export".equals(type)) {
                        compress(file, zos, name + "/" + file.getName(), type);
                    } else {
                        compress(file, zos, file.getName(), type);
                    }
                }
            }
        }
    }

    public static void unZip(String filePath, String destDirPath) {
        File srcFile = new File(filePath);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return;
        }
        // 开始解压
        try (ZipFile zipFile = new ZipFile(srcFile)) {
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + File.separator + entry.getName();
                    File dir = new File(dirPath);
                    if (dir.mkdirs()) {
                        logger.info(SUCCESS_MSG);
                    }
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + File.separator + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        boolean mkdirs = targetFile.getParentFile().mkdirs();
                        if (mkdirs) {
                            logger.info(SUCCESS_MSG);
                        }
                    }
                    if (targetFile.createNewFile()) {
                        logger.info(SUCCESS_MSG);
                    }
                    // 将压缩文件内容写入到这个文件中
                    try (InputStream is = zipFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(targetFile)) {
                        int len;
                        byte[] buf = new byte[BUFFER_SIZE];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
    }

    public static void isExistDirectory(String path) {
        File file = new File(path);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            boolean mkdirs = file.mkdirs();
            if (mkdirs) {
                logger.warn("{}文件不存在，已创建！", path);
            }
        }
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

    public static void copyFile2Target(String path, String outPath) throws IOException {
        int i;
        byte[] bytes = new byte[1024];
        File targetFile = new File(outPath);
        if (targetFile.createNewFile()) {
            File originFile = new File(path);
            try (FileOutputStream targetOutStream = new FileOutputStream(targetFile);
                 FileInputStream originInputStream = new FileInputStream(originFile)) {
                while ((i = originInputStream.read(bytes)) > -1) {
                    targetOutStream.write(bytes, 0, i);
                }
            }
        }
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

    public static void copyDir(String sourcePath, String newPath) {
        File file = new File(sourcePath);
        String[] filePath = file.list();
        File newPathFile = new File(newPath);
        boolean exists = newPathFile.exists();
        if (!exists) {
            boolean mkdir = newPathFile.mkdir();
            if (mkdir) {
                logger.info("文件夹创建成功");
            }
        }
        if (filePath == null) {
            return;
        }
        for (String path : filePath) {
            if ((new File(sourcePath + File.separator + path)).isDirectory()) {
                copyDir(sourcePath + File.separator + path, newPath + File.separator + path);
            }
            if (new File(sourcePath + File.separator + path).isFile()) {
                String content = FileUtil.readToString(sourcePath + File.separator + path);
                writeFile(newPath + File.separator + path, content, false);
            }
        }
    }

    public static String readZipFile(String path) throws IOException {
        ZipEntry zipEntry;
        String str = "";
        File file = new File(path);
        if (file.exists()) {
            //解决包内文件存在中文时的中文乱码问题
            try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path), Charset.forName("GBK"))) {
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.isDirectory()) {
                        //遇到文件夹就跳过
                        return zipEntry.getName().split("/")[0];
                    } else {
                        str = zipEntry.getName().substring(zipEntry.getName().lastIndexOf("\\") + 1);
                    }
                }
            }
        }
        return str;
    }
}
