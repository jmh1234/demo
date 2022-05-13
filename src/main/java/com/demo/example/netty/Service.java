package com.demo.example.netty;

import com.demo.util.LoggerUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * 服务端
 *
 * @author Ji MingHao
 * @since 2022-05-13 10:13
 */
@SuppressWarnings("all")
public class Service {

    private static final Logger logger = LoggerUtil.getInstance(Service.class);

    public static void main(String[] args) throws IOException {
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.configureBlocking(false);

            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            ssc.bind(new InetSocketAddress(8080));

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        ByteBuffer buffer = ByteBuffer.allocate(4);
                        sc.register(selector, SelectionKey.OP_READ, buffer);
                    } else if (key.isReadable()) {
                        try {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            int read = channel.read(buffer);
                            if (read == -1) {
                                key.cancel();
                            } else {
                                buffer.flip();
                                logger.info(" ====1====> {}", Charset.defaultCharset().decode(buffer));
                                if (buffer.position() == buffer.limit()) {
                                    ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() << 1);
                                    buffer.flip();
                                    newBuffer.put(buffer);
                                    key.attach(newBuffer);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            key.cancel();
                        }
                    }
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 将缓冲区的内容根据换行符分割
     * 解决黏包和分包问题
     *
     * @param source 缓存中的内容
     */
    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                logger.info(" ====2====> {}", Charset.defaultCharset().decode(target));
            }
        }
        source.compact();
    }
}
