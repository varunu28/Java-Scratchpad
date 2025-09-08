package com.varunu28.scratchpad.dependencyproblem;

public record DummySoftware(String softwareName) {

    public void install() {
        System.out.println("Installed software: " + softwareName);
    }
}
