package com.demo.util;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

public class Utils {
    private Utils() {

    }

    public static Map<String, Integer> getPageNumAndPageSize(HttpServletRequest request) {
        Map<String, Integer> resultMap = new HashMap<>();
        int pageSize;
        if (request.getParameter("pageSize") == null) pageSize = 1;
        else pageSize = Integer.valueOf(request.getParameter("pageSize"));

        int num;
        if (request.getParameter("pageNum") == null) num = 1;
        else num = Integer.valueOf(request.getParameter("pageNum"));
        int pageNum = pageSize == 1 ? num * pageSize : (num - 1) * pageSize;
        resultMap.put("pageNum", pageNum);
        resultMap.put("pageSize", pageSize);
        return resultMap;
    }

    public static List<Class> getAllInterfaceAchieveClass(Class<?> clazz) {
        ArrayList<Class> list = new ArrayList<>();
        //判断是否是接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class> allClass = getAllClassByPath(clazz.getPackage().getName());
                for (Class<?> klass : allClass) {
                    //排除抽象类
                    if (Modifier.isAbstract(klass.getModifiers())) continue;
                    //判断是不是同一个接口
                    if (clazz.isAssignableFrom(klass)) {
                        if (!clazz.equals(klass)) {
                            list.add(klass);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("出现异常");
            }
        }
        return list;
    }

    private static ArrayList<Class> getAllClassByPath(String packageName) {
        ArrayList<Class> list = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        try {
            ArrayList<File> fileList = new ArrayList<>();
            Enumeration<URL> enumeration = classLoader.getResources(path);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                fileList.add(new File(url.getFile()));
            }
            for (File file : fileList) {
                list.addAll(findClass(file, packageName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static ArrayList<Class> findClass(File file, String packageName) {
        ArrayList<Class> list = new ArrayList<>();
        if (!file.exists()) return list;
        File[] files = file.listFiles();
        if (files != null) {
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    //添加断言用于判断
                    assert !file2.getName().contains(".");
                    ArrayList<Class> arrayList = findClass(file2, packageName + "." + file2.getName());
                    if (arrayList != null) list.addAll(arrayList);
                } else if (file2.getName().endsWith(".class")) {
                    try {
                        //保存的类文件不需要后缀.class
                        list.add(Class.forName(packageName + '.' + file2.getName().substring(0,
                                file2.getName().length() - 6)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }
}
