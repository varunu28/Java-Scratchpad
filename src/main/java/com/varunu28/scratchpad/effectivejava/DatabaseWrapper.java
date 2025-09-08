package com.varunu28.scratchpad.effectivejava;

public class DatabaseWrapper {

    private static DatabaseWrapper instance;

    private DatabaseWrapper() {
        // Heavy computation
    }

    public static DatabaseWrapper getInstance() {
        if (instance == null) {
            instance = new DatabaseWrapper();
        }
        return instance;
    }
}
