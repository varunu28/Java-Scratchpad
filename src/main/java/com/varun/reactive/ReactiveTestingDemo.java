package com.varun.reactive;

import reactor.core.publisher.Flux;

import java.util.List;

public class ReactiveTestingDemo {
    
    public Flux<String> getStringFlux(List<String> data) {
        return Flux.fromIterable(data);
    }

    public Flux<String> getStringFluxWithError(List<String> data) {
        return Flux.concat(getStringFlux(data), Flux.error(new RuntimeException()));
    }

    public Flux<Integer> getIntegerFluxOfAnyTenElements() {
        return Flux.range(1, 10);
    }
}
