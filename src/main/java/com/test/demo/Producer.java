package com.test.demo;

import java.util.Optional;
import java.util.Random;

public class Producer extends Thread{
    private Container container;
    private final Object lock;

    Producer(Container container, Object lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (container.getValue().isPresent()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int i = new Random().nextInt();
                System.out.println("Producer " + i);
                container.setValue(Optional.of(i));

                lock.notify();
            }
        }
    }
}
