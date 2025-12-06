package com.varunu28.scratchpad.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {

    static void main() throws InterruptedException {

        CounterContainer counterContainer = new CounterContainer();

        Thread incrementingThread = new Thread(() -> {
            for (int i = 1; i <= 100000; i++) {
                counterContainer.increment();
            }
        });

        Thread decrementingThread = new Thread(() -> {
            for (int i = 1; i <= 100000; i++) {
                counterContainer.decrement();
            }
        });

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        IO.println("Final counter value should be 0: " + counterContainer.getCounter());
    }
}

/**
 * We use AtomicInteger to ensure all operations on the counter are atomic and thread-safe. This way we don't need
 * any other synchronization mechanism like synchronized blocks or locks.
 */
class CounterContainer {

    private final AtomicInteger counter;

    public CounterContainer() {
        this.counter = new AtomicInteger(0);
    }

    public void increment() {
        counter.incrementAndGet();
    }

    public void decrement() {
        counter.decrementAndGet();
    }

    public int getCounter() {
        return counter.get();
    }
}