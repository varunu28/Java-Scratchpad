package com.varunu28.scratchpad.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {

    static void main() throws InterruptedException {
        // Stack implementation that uses locks/synchronization ends up performing ~14000000 operations in 10 seconds
        // StandardStack<Integer> stack = new StandardStack<>();

        // Lock free Stack implementation ends up performing ~89000000 operations in 10 seconds which is ~6.3 times
        // more than the synchronized version.
        LockFreeStack<Integer> stack = new LockFreeStack<>();

        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            stack.push(random.nextInt());
        }

        List<Thread> threads = getThreads(stack, random);
        threads.forEach(Thread::start);
        Thread.sleep(10000);
        System.out.println("Standard Stack operation count: " + stack.getCounter());
    }

    private static List<Thread> getThreads(LockFreeStack<Integer> stack, Random random) {
        List<Thread> threads = new ArrayList<>();
        int numberOfPusherThreads = 2;
        int numberOfPopperThreads = 2;

        for (int i = 0; i < numberOfPusherThreads; i++) {
            Thread pusherThread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });

            pusherThread.setDaemon(true);
            threads.add(pusherThread);
        }

        for (int i = 0; i < numberOfPopperThreads; i++) {
            Thread popperThread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });

            popperThread.setDaemon(true);
            threads.add(popperThread);
        }
        return threads;
    }

    private static class LockFreeStack<T> {

        private final AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private final AtomicInteger counter = new AtomicInteger(0);

        public void push(T value) {
            StackNode<T> node = new StackNode<>(value);
            while (true) {
                StackNode<T> current = head.get();
                node.next = current;
                if (head.compareAndSet(current, node)) {
                    break;
                } else {
                    try {
                        Thread.sleep(0, 1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            counter.incrementAndGet();
        }

        public T pop() {
            StackNode<T> current = head.get();
            StackNode<T> newHeadNode;
            while (current != null) {
                newHeadNode = current.next;
                if (head.compareAndSet(current, newHeadNode)) {
                    break;
                } else {
                    try {
                        Thread.sleep(0, 1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    current = head.get();
                }
            }
            counter.incrementAndGet();
            return current != null ? current.value : null;
        }

        public int getCounter() {
            return counter.get();
        }
    }

    private static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        public int getCounter() {
            return counter;
        }

        public synchronized void push(T value) {
            StackNode<T> node = new StackNode<>(value);
            node.next = head;
            head = node;
            counter++;
        }

        public synchronized T pop() {
            if (head == null) {
                counter++;
                return null;
            }
            T value = head.value;
            head = head.next;
            counter++;
            return value;
        }
    }

    private static class StackNode<T> {
        public T value;
        public StackNode<T> next;

        public StackNode(T value) {
            this.value = value;
            this.next = null;
        }
    }
}
