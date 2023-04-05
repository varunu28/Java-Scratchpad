package com.varun.reactive;

import com.varun.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

class ReactiveMergeTest {

    private ReactiveMerge reactiveMerge;

    @BeforeEach
    public void setUp() {
        reactiveMerge = new ReactiveMerge();
    }

    @Test
    public void mergeFluxInterleaved_Success() {
        // Arrange
        List<User> users = List.of(
                new User("david_james", "david", "james"),
                new User("james_bond", "james", "bond")
        );
        Flux<User> fluxOne = Flux.just(users.get(0));
        Flux<User> fluxTwo = Flux.just(users.get(1));

        // Act
        Flux<User> result = reactiveMerge.mergeFluxInterleaved(fluxOne, fluxTwo);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(users::contains)
                .expectNextMatches(users::contains)
                .verifyComplete();
    }

    @Test
    public void mergeFluxWithOrdering_Success() {
        // Arrange
        List<User> users = List.of(
                new User("david_james", "david", "james"),
                new User("ron_mak", "ron", "mak"),
                new User("james_bond", "james", "bond")
        );
        Flux<User> fluxOne = Flux.just(users.get(0), users.get(1)).delayElements(Duration.ofMillis(5));
        Flux<User> fluxTwo = Flux.just(users.get(2));

        // Act
        Flux<User> result = reactiveMerge.mergeFluxWithOrdering(fluxOne, fluxTwo);

        // Assert
        StepVerifier.create(result)
                .expectNext(users.get(0))
                .expectNext(users.get(1))
                .expectNext(users.get(2))
                .verifyComplete();
    }
}