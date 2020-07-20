package com.test.demo.caseTest.thread;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class PrintThread {
    public static void main(String[] args) throws Exception {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        out.connect(in);  // 将输出流和输入流进行连接，否则在使用时会抛出IOException
        Thread printThread = new Thread(new Print(in), "PrintThread");
        printThread.start();
        int receive;
        try {
            while ((receive = System.in.read()) != -1) {
                out.write(receive);
            }
        } finally {
            out.close();
        }
    }

    static class Print implements Runnable {

        private PipedReader in;

        private Print(PipedReader in) {
            this.in = in;
        }

        public void run() {
            int receive;
            try {
                while ((receive = in.read()) != -1) {
                    System.out.print((char) receive);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
