package com.varunu28.scratchpad.concurrency;

public class ApiService {
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
        return new ApiService.Preference("Wheat");
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

    public record Preference(String breadType) {}
}
