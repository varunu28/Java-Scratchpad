package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierRunner {
    public static void main(String[] args) {
        int numberOfThreads = 200;
        List<Thread> threads = new ArrayList<>();
        Barrier barrier = new Barrier(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(new CoordinatedWorkRunner(barrier));
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static class Barrier {
        private final int numberOfWorkers;
        private final Semaphore semaphore = new Semaphore(0);
        private final Lock lock = new ReentrantLock();
        private int counter = 0;

        public Barrier(int numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
        }

        public void await() throws InterruptedException {
            lock.lock();
            boolean isLastWorker = false;
            try {
                counter++;
                if (counter == numberOfWorkers) {
                    isLastWorker = true;
                }
            } finally {
                lock.unlock();
            }
            if (isLastWorker) {
                semaphore.release(numberOfWorkers - 1);
            } else {
                semaphore.acquire();
            }
        }
    }

    public static class CoordinatedWorkRunner implements Runnable {

        private final Barrier barrier;

        public CoordinatedWorkRunner(Barrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println("Thread " + Thread.currentThread().getName() + " part 1 of the work is finished");
                barrier.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " part 2 of the work is finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
