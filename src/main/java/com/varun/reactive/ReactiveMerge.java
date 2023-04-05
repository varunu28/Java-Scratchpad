package com.varun.reactive;

import com.varun.model.User;
import reactor.core.publisher.Flux;

public class ReactiveMerge {

    Flux<User> mergeFluxInterleaved(Flux<User> fluxOne, Flux<User> fluxTwo) {
        return Flux.merge(fluxOne, fluxTwo);
    }

    Flux<User> mergeFluxWithOrdering(Flux<User> fluxOne, Flux<User> fluxTwo) {
        return Flux.concat(fluxOne, fluxTwo);
    }
}
