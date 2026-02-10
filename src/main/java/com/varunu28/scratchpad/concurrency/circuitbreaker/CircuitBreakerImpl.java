package com.varunu28.scratchpad.concurrency.circuitbreaker;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class CircuitBreakerImpl implements CircuitBreaker {

    /**
     * This is the period in which we track the number of errors. If the number of errors exceeds the threshold within
     * this window, we open the circuit.
     */
    private final long windowMillis;

    /**
     * The number of errors that must occur within the window to trigger the circuit to open.
     */
    private final int errorThreshold;

    /**
     * The amount of time the circuit remains open before allowing attempts to close it again.
     */
    private final long coolOffMillis;

    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
    private final ConcurrentLinkedQueue<Long> errorTimestamps = new ConcurrentLinkedQueue<>();
    private final AtomicLong lastOpenedTime = new AtomicLong(0);

    public CircuitBreakerImpl(int circuitBreakerWindowInMillis, int errorThreshold, int coolOffPeriodInMillis) {
        this.windowMillis = circuitBreakerWindowInMillis;
        this.errorThreshold = errorThreshold;
        this.coolOffMillis = coolOffPeriodInMillis;
    }

    @Override
    public void call(Function<String, String> request) {
        try {
            if (!allowRequest()) {
                System.out.println("Request failed fast due to OPEN circuit.");
                return;
            }
            String response = request.apply("Request Data");
            System.out.println("Request succeeded with response: " + response);
        } catch (Exception e) {
            System.out.println("Request failed with exception: " + e.getMessage());
            recordFailure();
        }
    }

    /**
     * Determines if a request should be allowed to proceed based on the current state of the circuit breaker.
     * If the circuit is OPEN, it checks if the cool-off period has passed. If it has, it attempts to reset the circuit
     * to CLOSED. If the circuit is still OPEN and cooling off, it returns false to indicate that the request should
     * fail fast.
     *
     * @return true if the request is allowed to proceed (circuit is CLOSED or has cooled off), false if it should
     * fail fast (circuit is OPEN and still cooling off).
     */
    private boolean allowRequest() {
        if (state.get() == State.OPEN) {
            long openedTime = lastOpenedTime.get();
            long now = System.currentTimeMillis();
            if (now - openedTime >= coolOffMillis) {
                if (lastOpenedTime.compareAndSet(openedTime, now)) {
                    reset();
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * If a request fails, we record the timestamp of the failure. We then clean up any old error timestamps that are
     * outside the current window. If the number of recent errors exceeds the threshold, we open the circuit.
     */
    private void recordFailure() {
        long now = System.currentTimeMillis();
        errorTimestamps.add(now);
        cleanOldErrors(now);
        if (errorTimestamps.size() >= errorThreshold) {
            openCircuit(now);
        }
    }

    /**
     * @param currentTime time in milliseconds to compare against the error timestamps. This method removes any error
     *                    timestamps that are older than the defined window (windowMillis) from the errorTimestamps
     *                    queue. This ensures that we only consider recent errors when determining whether to open
     *                    the circuit.
     */
    private void cleanOldErrors(long currentTime) {
        while (!errorTimestamps.isEmpty() &&
            (currentTime - errorTimestamps.peek() > windowMillis)) {
            errorTimestamps.poll();
        }
    }

    /**
     * @param now time in milliseconds when the circuit is being opened. This method attempts to change the state of
     *            the circuit from CLOSED to OPEN. If successful, it sets the lastOpenedTime to the current time and
     *            logs that the circuit has been opened. This method is called when the number of recent errors exceeds
     *            the defined threshold, indicating that the system is likely experiencing issues and should stop
     *            allowing requests to proceed until it has had time to recover (cool-off period).
     */
    private void openCircuit(long now) {
        if (state.compareAndSet(State.CLOSED, State.OPEN)) {
            lastOpenedTime.set(now);
            System.out.println("Circuit OPENED at " + Instant.ofEpochMilli(now));
        }
    }

    /**
     * We attempt to change the state of the circuit from OPEN to CLOSED. If successful, we clear the error timestamps
     * and log that the circuit has been closed. This method is called after the cool-off period has passed, allowing
     * the system to attempt to recover and start accepting requests again. By resetting the circuit, we give the
     * system a chance to stabilize before we start tracking errors again. If the circuit is still OPEN and the
     * cool-off period has not passed, we will continue to fail fast until the cool-off period is over, and we can
     * attempt to reset the circuit.
     */
    private void reset() {
        if (state.compareAndSet(State.OPEN, State.CLOSED)) {
            errorTimestamps.clear();
            System.out.println("Circuit CLOSED at " + Instant.now());
        }
    }

    enum State {
        CLOSED,
        OPEN
    }
}