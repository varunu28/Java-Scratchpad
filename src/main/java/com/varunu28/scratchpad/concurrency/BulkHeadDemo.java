package com.varunu28.scratchpad.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class BulkHeadDemo {
    static void main() {
        BulkHead bulkHead = new BulkHead(2);
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            for (int i = 1; i <= 5; i++) {
                final int requestId = i;
                executor.submit(() -> bulkHead.handleRequest(requestId));
            }
        }
    }
}

/**
 * A demonstration of the Bulkhead pattern using a Semaphore to limit concurrent access to a resource.
 * In this example, the BulkHead class allows only a certain number of concurrent requests to be handled at a time. If
 * the limit is exceeded, additional requests are rejected until a slot becomes available
 */
class BulkHead {
    private final Semaphore semaphore;

    public BulkHead(int capacity) {
        this.semaphore = new Semaphore(capacity);
    }

    public void handleRequest(int requestId) {
        if (semaphore.tryAcquire()) {
            try {
                System.out.println("Handling request " + requestId);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
                System.out.println("Finished handling request " + requestId);
            }
        } else {
            System.out.println("Request " + requestId + " rejected due to bulkhead limit.");
        }
    }
}
