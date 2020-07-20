package com.test.demo.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("all")
public class FileUtil {

    private static final int BUFFER_SIZE = 2 * 1024;
    private static Logger logger = LoggerUtil.getInstance(FileUtil.class);

    public static String readToString(String filePath, String encoding) {
        File file = new File(filePath);
        if (!file.exists()) return null;
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
            return new String(fileContent, encoding);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
            return null;
        }
    }

    public static String readToString(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) return null;
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        // 读取文件
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        // 任务结束返回结果的集合
        List<Future<String>> totalResult = new ArrayList<>();
        // 最后返回的集合
        List<String> finalResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int id = i;
            // 一个线程读取一行文字
            Future<String> singleResult = threadPool.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String line;
                    while ((line = reader.readLine()) != null) {
//                        System.out.println(Thread.currentThread().getName() + " ：" + id + " ：" + line);
//                        System.out.println(line);
                    }
                    return line;
                }
            });
            totalResult.add(singleResult);
        }

        for (Future<String> future : totalResult) {
            // 把每一个future对象里的map集合合并到 finalResult
            if (future.get() != null) finalResult.add(future.get());
        }

        threadPool.shutdown();
        return finalResult.stream().collect(Collectors.joining("\n"));
    }

    // 获取程序所在目录
    public static String getProgramDirPath() {
        URL url = FileUtil.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码
        } catch (Exception e) {
            // !! do not use : MyLog.logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar")) {// 可执行jar包运行的结果里包含".jar"
            // 截取路径中的jar包名
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        } else {
            //filePath = new File(new File(filePath).getParentFile().getParentFile().getParentFile(),"install").getAbsolutePath();
        }

        File file = new File(filePath);
        filePath = file.getAbsolutePath();//得到windows下的正确路径
        return filePath;
    }

    public static boolean checkExistAndAbord(String name) {
        File f = new File(name);
        if (!f.exists()) return false;
        else return true;
    }

    /**
     * 追加写入文件
     *
     * @param out_path
     * @param content
     */
    public static void WriteFile(String out_path, String content, boolean append) {
        PrintWriter pfp = null;
        try {
            File f = new File(out_path);
            if (!f.exists()) f.createNewFile();
            FileWriter fw = new FileWriter(f, append);
            BufferedWriter bw = new BufferedWriter(fw);
            pfp = new PrintWriter(bw);
            pfp.println(content);
        } catch (IOException e) {
            logger.error(LoggerUtil.handleException(e));
        } finally {
            pfp.close();
        }
    }

    public static void WriteFile(String path, String content, String encoding) {
        OutputStreamWriter osw = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file = new File(file.getParent());
                if (!file.exists()) file.mkdirs();
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
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
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
        Boolean KeepDirStructure;
        if ("export".equals(type)) {
            KeepDirStructure = true;
        } else {
            KeepDirStructure = false;
        }
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
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
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
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
            file.mkdirs();
            logger.warn(path + "文件不存在，已创建！");
        }
    }

    public static List<String> getFile(String path) {
        List<String> fileNameList = new ArrayList<>();
        File file = new File(path);
        File[] array = file.listFiles();
        if (array == null) return null;
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                fileNameList.add(array[i].getName());
            } else if (array[i].isDirectory()) {
                getFile(array[i].getPath());
            }
        }
        return fileNameList;
    }

    public static Map<String, String> getProjectAttrMap(String projectPath) {
        String jsonProject = FileUtil.readToString(projectPath + "\\project.json", "UTF-8");
        Map<String, String> projectInfoMap = (Map) JSONObject.parseObject(jsonProject);
        // integrationNum 表示软件版本号 demarcateNum 表示数据版本号
        Map<String, String> map = new HashMap<>();
        map.put("parts", projectInfoMap.get("partsNum"));
        map.put("parts2", projectInfoMap.get("partsNum2"));
        map.put("soft", projectInfoMap.get("integrationNum"));
        map.put("data", projectInfoMap.get("demarcateNum"));
        map.put("project", projectInfoMap.get("projName"));
        map.put("bootStandard", projectInfoMap.get("bootStandard"));
        return map;
    }

    public static void copyFile2Target(String path, String outPath) throws Exception {
        File afile = new File(path);
        File bfile = new File(outPath);//定义一个复制后的文件路径
        bfile.createNewFile();//新建文件
        FileInputStream c = new FileInputStream(afile);//定义FileInputStream对象
        FileOutputStream d = new FileOutputStream(bfile);//定义FileOutputStream对象
        byte[] bytes = new byte[1024];//定义一个byte数组
        int i = 0;
        while ((i = c.read(bytes)) > -1) {//判断有没有读取到文件末尾
            d.write(bytes, 0, i);//写数据
        }
        c.close();//关闭流
        d.close();//关闭流
    }

    public static void isChartPathExist(String dirPath, String type) throws Exception {
        File file = new File(dirPath);
        if (!file.exists()) {
            if ("folder".equals(type)) {
                file.mkdirs();
            } else if ("file".equals(type)) {
                file.createNewFile();
            }
        }
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            boolean mkdirs = file.mkdirs();
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
                    boolean delete = temp.delete();
                }
            } else if (path.contains("out\\padding")) {
                if (temp.isFile() && !tempFile.endsWith(".bat") && !tempFile.endsWith(".BAT")) {
                    boolean delete = temp.delete();
                }
            } else {
                if (temp.isFile()) {
                    boolean delete = temp.delete();
                }
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempFile);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempFile);// 再删除空文件夹
            }
        }
    }

    /***
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径
     */
    private static void delFolder(String folderPath) {
        delAllFile(folderPath); // 删除完里面所有内容
        String filePath = folderPath;
        filePath = filePath.toString();
        File myFilePath = new File(filePath);
        myFilePath.delete(); // 删除空文件夹
    }

    public static void copyDir(String sourcePath, String newPath) {
        File file = new File(sourcePath);
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }
        if (filePath == null) return;
        for (int i = 0; i < filePath.length; i++) {
            if ((new File(sourcePath + file.separator + filePath[i])).isDirectory()) {
                copyDir(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
            }

            if (new File(sourcePath + file.separator + filePath[i]).isFile()) {
                String s = FileUtil.readToString(sourcePath + file.separator + filePath[i], "UTF-8");
                FileUtil.WriteFile(newPath + file.separator + filePath[i], s, false);
            }
        }
    }

    public static String readZipFile(String path) throws Exception {
        ZipEntry zipEntry;
        String str = "";
        File file = new File(path);
        if (file.exists()) { //判断文件是否存在
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path), Charset.forName("GBK")); //解决包内文件存在中文时的中文乱码问题
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) { //遇到文件夹就跳过
                    return zipEntry.getName().split("/")[0];
                } else {
                    str = zipEntry.getName().substring(zipEntry.getName().lastIndexOf("\\") + 1);
                    //      System.out.println(zipEntry.getName().substring(zipEntry.getName().lastIndexOf("/")+1));//通过getName()可以得到文件名称
                }
            }
        }
        return str;
    }
}