package com.demo.example.netty;

import com.demo.util.LoggerUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * 客户端
 *
 * @author Ji MingHao
 * @since 2022-05-13 10:43
 */
public class Client {

    private static final Logger logger = LoggerUtil.getInstance(Client.class);

    public static void main(String[] args) throws IOException {
        try (SocketChannel sc = SocketChannel.open()) {
            sc.connect(new InetSocketAddress("localhost", 8080));
            sc.write(Charset.defaultCharset().encode("1221123121"));
        }
        logger.info("waiting...");
    }
}
