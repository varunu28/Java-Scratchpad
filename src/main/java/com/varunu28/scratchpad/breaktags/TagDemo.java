package com.varunu28.scratchpad.breaktags;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class TagDemo {

    static void main() {
        var list = new ArrayList<Integer>();
        IntStream.range(0, 10).forEach(list::add);

        in:
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (i * j > 10) {
                    IO.println(i * j);
                    break in;
                }
            }
        }
    }
}
