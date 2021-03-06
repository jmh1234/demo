package com.demo.example.reflect;

import java.io.File;
import java.io.FileInputStream;

public class MyClassLoader extends ClassLoader {
    // 存放字节码文件的目录
    private final File bytecodeFileDirectory;

    private MyClassLoader(File bytecodeFileDirectory) {
        this.bytecodeFileDirectory = bytecodeFileDirectory;
    }

    // 还记得类加载器是做什么的么？
    // "从外部系统中，加载一个类的定义（即Class对象）"
    // 请实现一个自定义的类加载器，将当前目录中的字节码文件加载成为Class对象
    // 提示，一般来说，要实现自定义的类加载器，你需要覆盖以下方法，完成：
    //
    // 1.如果类名对应的字节码文件存在，则将它读取成为字节数组
    //   1.1 调用ClassLoader.defineClass()方法将字节数组转化为Class对象
    // 2.如果类名对应的字节码文件不存在，则抛出ClassNotFoundException
    //
    // 一个用于测试的字节码文件可以在本项目的根目录找到
    //
    // 请思考：双亲委派加载模型在哪儿？为什么我们没有处理？
    // 扩展阅读：ClassLoader类的Javadoc文档
    @Override
    @SuppressWarnings("all")
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = bytecodeFileDirectory.getAbsolutePath() + "\\" + name + ".class";
        File file = new File(fileName);
        if (!file.exists()) throw new ClassNotFoundException(name);
        try {
            Long length = file.length();
            byte[] bytes = new byte[length.intValue()];
            FileInputStream in = new FileInputStream(fileName);
            in.read(bytes);
            return defineClass("com.demo." + name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        String filePath = "\\src\\main\\java\\com\\demo\\example\\reflect";
        String basedir = System.getProperty("basedir", System.getProperty("user.dir")) + filePath;
        File projectRoot = new File(basedir);
        MyClassLoader myClassLoader = new MyClassLoader(projectRoot);

        Class testClass = myClassLoader.loadClass("MyTestClass");
        Object testClassInstance = testClass.newInstance();
        String message = (String) testClass.getMethod("sayHello").invoke(testClassInstance);
        System.out.println(message);
    }
}
