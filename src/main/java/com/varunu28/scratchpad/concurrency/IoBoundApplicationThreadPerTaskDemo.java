package com.varunu28.scratchpad.concurrency;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.varunu28.scratchpad.concurrency.ApiService.blockingOperation;

/**
 * Thread-per task model is helpful for scaling the throughput of IO-bound applications. But at the same time we need
 * to understand that Threads are expensive resources, and we cannot create an unbounded number of threads in the
 * system. So we need to limit the number of threads in the pool based on the system capabilities.
 * <p>
 * Also increasing the number of threads beyond a certain limit will lead to excessive context switching which will
 * lead to performance degradation. This is also known as thread thrashing.
 * <p>
 * In this example, we are simulating an IO-bound application where each task performs 100 blocking operations. We
 * are using a fixed thread pool with a limited number of threads to handle the tasks. As each task is IO-bound, having
 * multiple threads will help in improving the throughput of the application. Though as we increase number of IO-bound
 * tasks, we need to be careful about the number of threads in the pool to avoid excessive context switching.
 */
public class IoBoundApplicationThreadPerTaskDemo {

    private static final int NUMBER_OF_TASKS = 10_000;

    static void main() {
        Scanner scanner = new Scanner(System.in);
        IO.println("Enter the input: ");
        scanner.nextLine();

        long start = System.currentTimeMillis();
        performTask();
        IO.println("Time taken: " + (System.currentTimeMillis() - start) + " ms");
    }

    // We have to limit the number of threads in the pool to avoid overwhelming the system. Having unbounded threads
    // will lead to excessive context switching and resource exhaustion eventually leading to JVM crash.
    private static void performTask() {
        try (ExecutorService executor = Executors.newFixedThreadPool(1000)) {
            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < 100; j++) {
                        blockingOperation();
                    }
                });
            }
        }
    }
}
