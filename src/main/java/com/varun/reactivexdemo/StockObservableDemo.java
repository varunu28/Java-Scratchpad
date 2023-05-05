package com.varun.reactivexdemo;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

/**
 * Code from tech talk on reactive programming
 * Link: <a href="https://www.youtube.com/watch?v=f3acAsSZPhU&ab_channel=Devoxx">Reactive Programming in Java</a>
 * */
public class StockObservableDemo {
    public static void main(String[] args) {
        List<String> symbols = Arrays.asList("GOOG", "AMZN", "ITC");

        Observable<StockInfo> feed = StockServer.getFeed(symbols);
        System.out.println("got observable...");

        feed
                .subscribe(
                    System.out::println,
                    err -> System.out.println("ERROR: " + err.getMessage()),
                    () -> System.out.println("DONE"));
    }
}
