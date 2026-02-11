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
    private final long errorThreshold;

    /**
     * The amount of time the circuit remains open before allowing attempts to close it again.
     */
    private final long coolOffMillis;

    /**
     * The number of successful requests required in the HALF_OPEN state to transition back to CLOSED. This allows us
     * to ensure that the system has recovered before we start accepting requests again. If the number of successful
     * requests in the HALF_OPEN state reaches this threshold, we will close the circuit and start accepting requests
     * again. If the circuit is still OPEN and the cool-off period has not passed, we will continue to fail fast
     * until the cool-off period is over, and we can attempt to reset the circuit.
     */
    private final long halfOpenSuccessThreshold;

    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
    private final ConcurrentLinkedQueue<Long> errorTimestamps = new ConcurrentLinkedQueue<>();
    private final AtomicLong halfOpenSuccessCount = new AtomicLong(0);
    private final AtomicLong lastOpenedTime = new AtomicLong(0);

    public CircuitBreakerImpl(
        long circuitBreakerWindowInMillis, long errorThreshold, long coolOffPeriodInMillis,
        long halfOpenSuccessThreshold) {
        this.windowMillis = circuitBreakerWindowInMillis;
        this.errorThreshold = errorThreshold;
        this.coolOffMillis = coolOffPeriodInMillis;
        this.halfOpenSuccessThreshold = halfOpenSuccessThreshold;
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
            recordSuccess();
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
                    if (state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                        halfOpenSuccessCount.set(0);
                        System.out.println("Circuit HALF_OPEN at " + Instant.ofEpochMilli(now));
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private void recordSuccess() {
        if (state.get() == State.HALF_OPEN) {
            long successCount = halfOpenSuccessCount.incrementAndGet();
            if (successCount >= halfOpenSuccessThreshold) {
                closeCircuit();
            }
        }
    }

    /**
     * If a request fails, we record the timestamp of the failure. If the circuit is currently in the HALF_OPEN state,
     * we immediately transition to OPEN since we have seen a failure during the test phase and add the error timestamp.
     * For CLOSED state, we add the current timestamp to the errorTimestamps queue. This queue is used to track the
     * timestamps of recent errors.
     * We then call cleanOldErrors to remove any timestamps that are outside the defined window (windowMillis). This
     * ensures that we are only considering recent errors when determining whether to open the circuit. If the number
     * of recent errors (the size of the errorTimestamps queue) exceeds the defined error threshold, we call openCircuit
     * to transition the circuit to the OPEN state, which will cause subsequent requests to fail fast until the
     * cool-off period has passed, and we can attempt to transition to HALF_OPEN.
     */
    private void recordFailure() {
        long now = System.currentTimeMillis();
        if (state.get() == State.HALF_OPEN) {
            if (state.compareAndSet(State.HALF_OPEN, State.OPEN)) {
                lastOpenedTime.set(now);
                halfOpenSuccessCount.set(0);
                errorTimestamps.add(now);
                System.out.println("Circuit OPENED from HALF_OPEN at " + Instant.ofEpochMilli(now));
            }
            return;
        }
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
            halfOpenSuccessCount.set(0);
            System.out.println("Circuit OPENED at " + Instant.ofEpochMilli(now));
        }
    }

    /**
     * We attempt to change the state of the circuit from HALF_OPEN to CLOSED. If successful, we clear the error
     * timestamps and log that the circuit has been closed. We also reset the half open count so that it can be reused.
     * This method is called after we have seen number of successful requests in HALF_OPEN state that exceeds
     * halfOpenSuccessThreshold.
     */
    private void closeCircuit() {
        if (state.compareAndSet(State.HALF_OPEN, State.CLOSED)) {
            errorTimestamps.clear();
            halfOpenSuccessCount.set(0);
            System.out.println("Circuit CLOSED at " + Instant.now());
        }
    }

    enum State {
        CLOSED,
        HALF_OPEN,
        OPEN
    }
}