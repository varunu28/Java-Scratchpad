package com.varunu28.scratchpad.concurrency;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadDemo {

    private static final int NUMBER_OF_TASKS = 10_000;

    static void main() {
        Scanner scanner = new Scanner(System.in);
        IO.println("Enter the input: ");
        scanner.nextLine();

        long start = System.currentTimeMillis();
        performTask();
        IO.println("Time taken: " + (System.currentTimeMillis() - start) + " ms");
    }

    private static void performTask() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < 100; j++) {
                        ApiService.blockingOperation();
                    }
                });
            }
        }
    }
}
