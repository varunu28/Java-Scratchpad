package com.varunu28.scratchpad.concurrency;

import java.util.concurrent.StructuredTaskScope;

public class StructuredConcurrencyDemo {

    static void main() throws InterruptedException {
        ApiService apiService = new ApiService();
        ApiService.Preference pref = apiService.getPreferences();

        try (var scope = StructuredTaskScope.open()) {
            var beerTask = scope.fork(() -> apiService.getBread(pref.breadType()));
            var cheeseTask = scope.fork(apiService::getCheese);

            scope.join();

            IO.println("Order Details: " + beerTask.get() + " with " + cheeseTask.get());
        }
    }
}
