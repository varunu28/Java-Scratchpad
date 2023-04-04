package com.varun.reactive;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static com.varun.reactive.BlockUtil.blockForTermination;

public class ReactiveFluxDemo {
    public static void main(String[] args) throws IOException {
        // Buffer groups processed items from the flux into batches based upon the buffer size
        Flux<List<Integer>> processedBuffer = Flux.range(5, 3)
                .map(i -> i + 3)
                .filter(i -> i % 2 == 0)
                .buffer(3);

        processedBuffer.subscribe(System.out::println);

        // An example of backpressure where we request 5 items at a time and then request 1 item each time when we
        // consume an element.
        Flux.range(1, 100)
                .onBackpressureBuffer(10)
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        subscription.request(5);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println("Value: " + value);
                        request(1);
                    }
                });

        // An example of Mono where a list of Mono is used and the parent mono returns the value returned by first
        // available Mono in the list
        Mono.firstWithValue(
                Mono.just(1).map(i -> "foo" + i),
                Mono.delay(Duration.ofMillis(10)).thenReturn("bar")
        ).subscribe(System.out::println);

        blockForTermination();
    }
}
