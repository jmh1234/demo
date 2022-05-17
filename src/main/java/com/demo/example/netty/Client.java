package com.demo.example.netty;

import lombok.extern.log4j.Log4j2;

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
@Log4j2
public class Client {
    public static void main(String[] args) throws IOException {
        try (SocketChannel sc = SocketChannel.open()) {
            sc.connect(new InetSocketAddress("localhost", 8080));
            sc.write(Charset.defaultCharset().encode("1221123121"));
        }
        log.info("waiting...");
    }
}
