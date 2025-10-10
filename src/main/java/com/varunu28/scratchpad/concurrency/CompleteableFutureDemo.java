package com.varunu28.scratchpad.concurrency;

import java.util.concurrent.CompletableFuture;

/**
 * A demo class to showcase the usage of CompleteableFuture in Java.
 */
public class CompleteableFutureDemo {

    static void main() {
        // basic chaining of completable futures
        CompletableFuture<String> futureOne = new CompletableFuture<>();

        // Chaining futureOne to finalFuture so we can build a chain of operations
        CompletableFuture<String> finalFuture = futureOne.thenApply(result -> {
            System.out.println("intermediate result:" + result);
            return "final result";
        });

        // creating another CompleteableFuture to demonstrate thenCompose within a CompleteableFuture
        futureOne.thenCompose(result -> CompletableFuture.completedFuture(result.toUpperCase()));

        System.out.println("Number of dependenets for futureOne: " + futureOne.getNumberOfDependents());
        System.out.println("Number of dependenets for finalFuture: " + finalFuture.getNumberOfDependents());

        finalFuture.whenComplete((result, ignored) -> System.out.println(result));

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            futureOne.complete("Hello World");
        }).start();

        System.out.println("Main thread is running...");
    }
}
