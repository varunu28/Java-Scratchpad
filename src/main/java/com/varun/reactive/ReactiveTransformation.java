package com.varun.reactive;

import com.varun.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveTransformation {

    public Mono<User> capitalizeMono(Mono<User> mono) {
        return mono.map(
          user -> new User(user.username().toUpperCase(), user.firstname().toUpperCase(), user.lastname().toUpperCase())
        );
    }

    public Flux<User> capitalizeFlux(Flux<User> flux) {
        return flux.map(
            user -> new User(user.username().toUpperCase(), user.firstname().toUpperCase(), user.lastname().toUpperCase())
        );
    }

    public Flux<User> capitalizeFluxAsync(Flux<User> flux) {
        return flux.flatMap(this::asyncCapitalize);
    }

    private Mono<User> asyncCapitalize(User user) {
        return Mono.just(
                new User(user.username().toUpperCase(), user.firstname().toUpperCase(), user.lastname().toUpperCase()));
    }
}
