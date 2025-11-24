package com.varunu28.scratchpad.concurrency;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

/**
 * A demo class to showcase the usage of CompleteableFuture in Java.
 */
public class CompleteableFutureDemo {

    static void main() {
        ApiService apiService = new ApiService();

        // Start 2 asynchronous tasks to fetch:
        // - preferences followed by fetching bread based on preferences
        // - cheese
        CompletableFuture<ApiService.Preference> preferences = supplyAsync(
            apiService::getPreferences, newVirtualThreadPerTaskExecutor());
        CompletableFuture<String> cheese = supplyAsync(apiService::getCheese, newVirtualThreadPerTaskExecutor());
        CompletableFuture<String> bread =
            preferences.thenApplyAsync(
                pref -> apiService.getBread(
                    pref.breadType()), newVirtualThreadPerTaskExecutor());

        IO.println("Order Details: " + bread.join() + " with " + cheese.join());
    }

    static class ApiService {

        public ApiService() {}

        private static void sleep(long durationInMillis) {
            try {
                Thread.sleep(durationInMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public Preference getPreferences() {
            sleep(1000);
            IO.println("Fetched Preferences in thread: " + Thread.currentThread());
            return new Preference("Wheat");
        }

        public String getBread(String breadType) {
            sleep(2000);
            IO.println("Fetched Bread in thread: " + Thread.currentThread());
            return breadType + " Bread";
        }

        public String getCheese() {
            sleep(1500);
            IO.println("Fetched Cheese in thread: " + Thread.currentThread());
            return "Fresh Cheddar Cheese";
        }

        record Preference(String breadType) {}
    }
}
