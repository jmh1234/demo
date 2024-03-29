package com.demo.example.thread;

/**
 * Created with IntelliJ IDEA.
 * Boss
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class Boss {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Object lock = new Object();
        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }
}
