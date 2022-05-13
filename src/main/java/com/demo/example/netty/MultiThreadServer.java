package com.demo.example.netty;

import com.demo.util.LoggerUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * 多线程实现服务端
 *
 * @author Ji MingHao
 * @since 2022-05-13 15:05
 */
@SuppressWarnings("all")
public class MultiThreadServer {

    private static final Logger logger = LoggerUtil.getInstance(MultiThreadServer.class);

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("Boss");
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.configureBlocking(false);
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            ssc.bind(new InetSocketAddress(8080));

            Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
            for (int i = 0; i < workers.length; i++) {
                workers[i] = new Worker("work-" + i);
            }

            AtomicInteger number = new AtomicInteger();
            while (true) {
                selector.select();
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        final SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        final int execute = number.getAndIncrement() % workers.length;
                        final Worker worker = workers[execute];
                        worker.register(sc);
                        sc.register(selector, SelectionKey.OP_READ);
                    }
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 工作线程，负责数据的读
     */
    public static class Worker implements Runnable {
        private final String name;
        private Selector selector;
        private volatile boolean start = false;
        private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }

        /**
         * 注册工作线程
         *
         * @param sc SocketChannel
         * @throws IOException IOException
         */
        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                selector = Selector.open();
                Thread thread = new Thread(this, name);
                thread.start();
                start = true;
            }
            // 将线程放到队列中实现线程之间的通信
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
                    final Runnable poll = queue.poll();
                    if (poll != null) {
                        poll.run();
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        final SelectionKey key = iterator.next();
                        if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            int read = channel.read(buffer);
                            if (read == -1) {
                                key.cancel();
                            } else {
                                buffer.flip();
                                logger.info(" ================>  " + Charset.defaultCharset().decode(buffer));
                            }
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
