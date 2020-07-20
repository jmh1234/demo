package com.test.demo;

import java.util.Optional;

public class Consumer extends Thread {
    private Container container;
    private final Object lock;

    Consumer(Container container, Object lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (!container.getValue().isPresent()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer integer = container.getValue().get();
                container.setValue(Optional.empty());
                System.out.println("Consumer " + integer);
                lock.notify();
            }
        }
    }
}
