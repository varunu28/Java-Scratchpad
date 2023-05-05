package com.varun.reactivexdemo;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import java.util.List;
import java.util.Random;

public class StockServer {

    public static Observable<StockInfo> getFeed(List<String> symbols) {
        System.out.println("created...");
        return Observable.create(emitter -> emitPrice(emitter, symbols));
    }

    private static void emitPrice(ObservableEmitter<StockInfo> emitter, List<String> symbols) {
        System.out.println("ready to emit");

        var count = 0;

        while (count < 5) {
            symbols.stream()
                    .map(StockInfo::fetch)
                    .forEach(emitter::onNext);

            sleep(1000);
            count++;
        }

        emitter.onComplete();
        // As complete signal is emitted below data won't be transmitted
        emitter.onNext(new StockInfo("blah", 0));
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

record StockInfo(String s, int price) {
    public static StockInfo fetch(String s) {
        int stockPrice = new Random().nextInt(1, 100);
        if (stockPrice < 10) {
            throw new RuntimeException("too low stock price");
        }
        return new StockInfo(s, stockPrice);
    }
}