package com.varunu28.scratchpad.effectivejava.singleton;

public class CustomParser {

    private static final CustomParser INSTANCE = new CustomParser();

    private CustomParser() {
        System.out.println("Instantiating parser");
        // Instantiation logic
    }

    public static CustomParser getInstance() {
        return INSTANCE;
    }
}
