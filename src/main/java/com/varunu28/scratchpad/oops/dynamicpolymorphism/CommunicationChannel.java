package com.varunu28.scratchpad.oops.dynamicpolymorphism;

public interface CommunicationChannel {

    default void communicate() {
        System.out.println("Communication channel started within interface");
    }
}
