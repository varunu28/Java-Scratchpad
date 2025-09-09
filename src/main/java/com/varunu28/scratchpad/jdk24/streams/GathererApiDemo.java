package com.varunu28.scratchpad.jdk24.streams;

import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

/**
 * Stream Gatherers API provides a set of methods to transform a stream into a collection of intermediate results.
 * <a href="https://docs.oracle.com/en/java/javase/23/docs/api/java.base/java/util/stream/Gatherers.html">
 * API Documentation
 * </a>
 */
public class GathererApiDemo {
    static void main() {
        List<Integer> numbers = IntStream.rangeClosed(1, 10).boxed().toList();
        System.out.println(numbers);

        // Folding the stream results in a single value.
        Integer sum = numbers.stream()
            .gather(Gatherers.fold(() -> 0, Integer::sum))
            .findFirst()
            .orElse(0);
        System.out.println(sum);

        // Scanning the stream results in a list of intermediate values.
        List<Integer> scanned = numbers.stream()
            .gather(Gatherers.scan(() -> 0, Integer::sum))
            .toList();
        System.out.println(scanned);

        // Concurrently mapping the stream results in a list of computed values.
        List<Integer> concurrentMappedSquares = numbers.stream()
            .gather(Gatherers.mapConcurrent(4, x -> x * x))
            .toList();
        System.out.println(concurrentMappedSquares);

        List<Double> temperatures = List.of(
            65.2, 64.8, 63.5, 61.2, 59.8,
            58.5, 57.9, 58.3, 59.1, 60.5,
            62.1, 63.8, 64.5, 65.0, 64.7
        );

        // Windowing the stream into fixed size windows and then computing the average for each window.
        List<List<Double>> fixedWindows = temperatures.stream()
            .gather(Gatherers.windowFixed(3))
            .toList();
        fixedWindows.forEach(window -> {
            double average = window.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
            System.out.printf("Window %s has average %.2f°C\n", window, average);
        });

        // Windowing the stream into sliding windows of fixed size and then computing the min & max for each window.
        List<List<Double>> slidingWindows = temperatures.stream()
            .gather(Gatherers.windowSliding(4))
            .toList();
        slidingWindows.forEach(window -> {
            double min = window.stream().mapToDouble(d -> d).min().orElse(0);
            double max = window.stream().mapToDouble(d -> d).max().orElse(0);
            System.out.printf(
                "Window : [%.1f...%.1f] has min %.2f°C and max %.2f°C\n",
                window.getFirst(), window.getLast(), min, max);
        });
    }
}
