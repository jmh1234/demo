package com.demo.util;

import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
@SuppressWarnings("unused")
public class FileUtil {

    private FileUtil() {

    }

    private static final int BUFFER_SIZE = 2 * 1024;
    private static final Logger logger = LoggerUtil.getInstance(FileUtil.class);

    public static String readToString(String filePath, String encoding) {
        File file = new File(filePath);
        if (!file.exists()) return null;
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            int read = in.read(fileContent);
            System.out.println(read);
            in.close();
            return new String(fileContent, encoding);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
            return null;
        }
    }

    /**
     * 追加写入文件
     *
     * @param out_path 文件输出路径
     * @param content  文件内容
     * @param append   是否追加写入文件
     */
    public static void WriteFile(String out_path, String content, boolean append) {
        PrintWriter pfp = null;
        try {
            File f = new File(out_path);
            if (!f.exists()) {
                if (f.createNewFile())
                    logger.info("文件创建成功！");
            }
            FileWriter fw = new FileWriter(f, append);
            BufferedWriter bw = new BufferedWriter(fw);
            pfp = new PrintWriter(bw);
            pfp.println(content);
        } catch (IOException e) {
            logger.error(LoggerUtil.handleException(e));
        } finally {
            if (pfp != null) pfp.close();
        }
    }

    public static void WriteFile(String path, String content, String encoding) {
        OutputStreamWriter osw = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file = new File(file.getParent());
                if (!file.exists()) {
                    if (file.mkdirs())
                        logger.info("文件创建成功！");
                }
            }
            osw = new OutputStreamWriter(new FileOutputStream(path), encoding);
            osw.write(content);
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
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

    public static void toZip(String srcDir, OutputStream out, String type) throws RuntimeException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), type);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    logger.error(LoggerUtil.handleException(e));
                }
            }
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name, String type) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        Boolean KeepDirStructure = "export".equals(type);
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(), type);
                    } else {
                        compress(file, zos, file.getName(), type);
                    }
                }
            }
        }
    }

    public static void unZip(String filePath, String destDirPath) throws RuntimeException {
        File srcFile = new File(filePath);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
        }
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + "/" + entry.getName();
                    File dir = new File(dirPath);
                    if (dir.mkdirs())
                        logger.info("文件创建成功！");
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        if (targetFile.getParentFile().mkdirs())
                            logger.info("文件创建成功！");
                    }
                    if (targetFile.createNewFile())
                        logger.info("文件创建成功！");
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            logger.error("unzip error from ZipUtils", e);
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void isExistDirectory(String path) {
        File file = new File(path);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            if (file.mkdirs())
                logger.warn(path + "文件不存在，已创建！");
        }
    }

    public static List<String> getFile(String path) {
        List<String> fileNameList = new ArrayList<>();
        File originFile = new File(path);
        File[] files = originFile.listFiles();
        if (files == null) return null;
        for (File file : files) {
            if (file.isFile()) {
                fileNameList.add(file.getName());
            } else if (file.isDirectory()) {
                getFile(file.getPath());
            }
        }
        return fileNameList;
    }

    public static void copyFile2Target(String path, String outPath) throws Exception {
        int i;
        byte[] bytes = new byte[1024];
        File targetFile = new File(outPath);
        if (targetFile.createNewFile()) {
            File originFile = new File(path);
            FileOutputStream targetOutStream = new FileOutputStream(targetFile);
            FileInputStream originInputStream = new FileInputStream(originFile);
            while ((i = originInputStream.read(bytes)) > -1) {
                targetOutStream.write(bytes, 0, i);
            }
            originInputStream.close();
            targetOutStream.close();
        }
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     *
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            if (file.mkdirs())
                logger.warn(path + "文件不存在，已创建！");
            return;
        }
        if (!file.isDirectory()) return;
        String[] tempList = file.list();
        File temp;
        if (tempList == null) return;
        for (String tempFile : tempList) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempFile);
            } else {
                temp = new File(path + File.separator + tempFile);
            }

            if (path.endsWith("sourcecode")) {
                if (temp.isFile() && !tempFile.endsWith(".txt") && !tempFile.endsWith(".TXT")) {
                    if (temp.delete())
                        logger.info("文件删除成功");
                }
            } else if (path.contains("out\\padding")) {
                if (temp.isFile() && !tempFile.endsWith(".bat") && !tempFile.endsWith(".BAT")) {
                    if (temp.delete())
                        logger.info("文件删除成功");
                }
            } else {
                if (temp.isFile()) {
                    if (temp.delete())
                        logger.info("文件删除成功");
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
        if (myFilePath.delete())
            logger.info("文件删除成功");
    }

    public static void copyDir(String sourcePath, String newPath) {
        File file = new File(sourcePath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            if ((new File(newPath)).mkdir()) {
                logger.info("文件夹创建成功");
            }
        }
        if (filePath == null) return;
        for (String path : filePath) {
            if ((new File(sourcePath + File.separator + path)).isDirectory()) {
                copyDir(sourcePath + File.separator + path, newPath + File.separator + path);
            }
            if (new File(sourcePath + File.separator + path).isFile()) {
                String content = FileUtil.readToString(sourcePath + File.separator + path, "UTF-8");
                FileUtil.WriteFile(newPath + File.separator + path, content, false);
            }
        }
    }

    public static String readZipFile(String path) throws Exception {
        ZipEntry zipEntry;
        String str = "";
        File file = new File(path);
        if (file.exists()) {
            //解决包内文件存在中文时的中文乱码问题
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path), Charset.forName("GBK"));
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    //遇到文件夹就跳过
                    return zipEntry.getName().split("/")[0];
                } else {
                    str = zipEntry.getName().substring(zipEntry.getName().lastIndexOf("\\") + 1);
                }
            }
        }
        return str;
    }
}
