package com.varunu28.scratchpad.concurrency.circuitbreaker;

import java.util.function.Function;

public interface CircuitBreaker {

    /**
     * @param request the request to be executed. The function takes a String input (representing the request data)
     *                and returns a String output (representing the response). The implementation of this function will
     *                simulate the actual work being done, which may succeed or fail. The circuit breaker will monitor
     *                the success and failure of these requests to determine when to open or close the circuit.
     */
    void call(Function<String, String> request);
}
