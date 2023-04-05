package com.varun.reactive;

import com.varun.reactive.ReactiveTransformation.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class ReactiveTransformationTest {

    private ReactiveTransformation reactiveTransformation;

    @BeforeEach
    public void setUp() {
        this.reactiveTransformation = new ReactiveTransformation();
    }

    @Test
    public void capitalizeMono_success() {
        // Arrange
        User user = new User("david_james", "david", "james");

        // Act
        Mono<User> capitalize = reactiveTransformation.capitalizeMono(Mono.just(user));

        // Assert
        User capatilizedUser = new User("DAVID_JAMES", "DAVID", "JAMES");
        StepVerifier.create(capitalize)
                .expectNext(capatilizedUser)
                .verifyComplete();
    }

    @Test
    public void capitalizeFlux_success() {
        // Arrange
        Flux<User> flux = Flux.just(
            new User("david_james", "david", "james"),
            new User("james_bond", "james", "bond")
        );

        // Act
        Flux<User> capitalize = reactiveTransformation.capitalizeFlux(flux);

        // Assert
        List<User> capitalizedUsers = List.of(
                new User("DAVID_JAMES", "DAVID", "JAMES"),
                new User("JAMES_BOND", "JAMES", "BOND")
        );
        StepVerifier.create(capitalize)
                .expectNext(capitalizedUsers.get(0), capitalizedUsers.get(1))
                .verifyComplete();
    }

    @Test
    public void capitalizeFluxAsync_success() {
        // Arrange
        Flux<User> flux = Flux.just(
                new User("david_james", "david", "james"),
                new User("james_bond", "james", "bond")
        );

        // Act
        Flux<User> capitalize = reactiveTransformation.capitalizeFluxAsync(flux);

        // Assert
        List<User> capitalizedUsers = List.of(
                new User("DAVID_JAMES", "DAVID", "JAMES"),
                new User("JAMES_BOND", "JAMES", "BOND")
        );
        StepVerifier.create(capitalize)
                .expectNextMatches(capitalizedUsers::contains)
                .expectNextMatches(capitalizedUsers::contains)
                .verifyComplete();
    }
}