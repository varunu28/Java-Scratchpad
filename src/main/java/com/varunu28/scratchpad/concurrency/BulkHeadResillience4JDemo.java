package com.varunu28.scratchpad.concurrency;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import java.time.Duration;
import java.util.function.Supplier;

public class BulkHeadResillience4JDemo {

    static void main() {
        Bulkhead bulkHead = getBulkhead();

        Supplier<String> decoratedSupplier = getStringSupplier(bulkHead);

        runDemo(decoratedSupplier);
    }

    private static Bulkhead getBulkhead() {
        BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
            .maxConcurrentCalls(2) // Only 2 concurrent calls allowed
            .maxWaitDuration(Duration.ZERO) // No waiting, immediately reject if limit is reached
            .build();

        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.of(bulkheadConfig);
        return bulkheadRegistry.bulkhead("demoBulkhead");
    }

    private static Supplier<String> getStringSupplier(Bulkhead bulkHead) {
        Supplier<String> demoSupplier = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Request processed";
        };

        return Bulkhead.decorateSupplier(bulkHead, demoSupplier);
    }

    private static void runDemo(Supplier<String> decoratedSupplier) {
        for (int i = 1; i <= 5; i++) {
            final int requestId = i;
            new Thread(() -> {
                try {
                    String result = decoratedSupplier.get();
                    System.out.println("Request " + requestId + ": " + result);
                } catch (Exception e) {
                    System.out.println("Request " + requestId + " failed: " + e.getMessage());
                }
            }).start();
        }
    }
}
