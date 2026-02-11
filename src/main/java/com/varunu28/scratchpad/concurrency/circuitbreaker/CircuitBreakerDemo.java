package com.varunu28.scratchpad.concurrency.circuitbreaker;

public class CircuitBreakerDemo {

    static void main() {
        CircuitBreaker circuitBreaker = new CircuitBreakerImpl(100L, 3L, 50L, 2L);

        for (int i = 1; i <= 10; i++) {
            if (i < 5 || i >= 7) {
                circuitBreaker.call(_ -> {
                    throw new RuntimeException("Simulated failure");
                });
            } else if (i >= 5 && i < 7) {
                circuitBreaker.call(request -> "Successful response for: " + request);
            }
            if (i == 5) {
                try {
                    Thread.sleep(100L); // Sleep for 1 minute to allow the circuit breaker to reset
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
