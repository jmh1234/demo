package com.demo.example.thread;

import java.util.Optional;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * Producer
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class Producer extends Thread {

    private final Container container;
    private final Object lock;
    private final Random random = new Random();

    Producer(Container container, Object lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (lock) {
                while (container.getValue().isPresent()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer r = random.nextInt();
                System.out.println("Producer " + r);
                container.setValue(Optional.of(r));
                lock.notifyAll();
            }
        }
    }
}
