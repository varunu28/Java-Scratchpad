package com.varunu28.scratchpad.reflection.jsonserializer.data;

public class Actor {
    private final String name;
    private final String[] knownForMovies;

    public Actor(String name, String[] knownForMovies) {
        this.name = name;
        this.knownForMovies = knownForMovies;
    }
}
