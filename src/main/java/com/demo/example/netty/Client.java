package com.demo.example.netty;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
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

    @SneakyThrows
    public static void main(String[] args) {
        final SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        final SocketAddress localAddress = sc.getLocalAddress();
        sc.write(Charset.defaultCharset().encode("122"));
        System.out.println("waiting...");
//        sc.close();
        System.in.read();
    }
}
