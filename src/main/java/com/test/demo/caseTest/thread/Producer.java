package com.test.demo.caseTest.thread;

import java.util.Optional;
import java.util.Random;

public class Producer extends Thread {
    private Container container;
    private final Object lock;

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
                int r = new Random().nextInt();
                System.out.println("Producer " + r);
                container.setValue(Optional.of(r));
                lock.notify();
            }
        }
    }
}
