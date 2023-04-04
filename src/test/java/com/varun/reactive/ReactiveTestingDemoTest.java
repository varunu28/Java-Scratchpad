package com.varun.reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReactiveTestingDemoTest {

    private static final List<String> DATA = List.of("foo", "bar");

    private ReactiveTestingDemo reactiveTestingDemo;
    
    @BeforeEach
    public void setUp() {
        reactiveTestingDemo = new ReactiveTestingDemo();
    }
    
    @Test
    public void getFooBar_success() {
        Flux<String> response = reactiveTestingDemo.getStringFlux(DATA);

        StepVerifier.create(response)
                .expectNext("foo")
                .expectNext("bar")
                .verifyComplete();
    }

    @Test
    public void getFooBar_errorSuccess() {
        Flux<String> response = reactiveTestingDemo.getStringFluxWithError(DATA);

        StepVerifier.create(response)
                .expectNext("foo")
                .expectNext("bar")
                .expectError(RuntimeException.class);
    }

    @Test
    public void getFooBar_assertSuccess() {
        Flux<String> response = reactiveTestingDemo.getStringFlux(DATA);

        StepVerifier.create(response)
                .assertNext(i -> assertTrue(i.endsWith("o")))
                .assertNext(i -> assertTrue(i.endsWith("r")))
                .verifyComplete();
    }

    @Test
    public void getAnyTenIntegers_success() {
        Flux<Integer> response = reactiveTestingDemo.getIntegerFluxOfAnyTenElements();

        StepVerifier.create(response)
                .expectNextCount(10)
                .verifyComplete();
    }
}