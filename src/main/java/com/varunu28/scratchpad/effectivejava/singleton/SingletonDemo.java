package com.varunu28.scratchpad.effectivejava.singleton;

public class SingletonDemo {

    public static void main(String[] args) {
        System.out.println(CustomParser.getInstance());
        System.out.println(CustomParser.getInstance());
    }
}
