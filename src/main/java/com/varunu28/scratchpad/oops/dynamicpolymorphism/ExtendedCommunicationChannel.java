package com.varunu28.scratchpad.oops.dynamicpolymorphism;

public class ExtendedCommunicationChannel extends ConcreteCommunicationChannel {

    @Override
    public void communicate() {
        System.out.println("Communication channel started within ExtendedCommunicationChannel");
    }
}
