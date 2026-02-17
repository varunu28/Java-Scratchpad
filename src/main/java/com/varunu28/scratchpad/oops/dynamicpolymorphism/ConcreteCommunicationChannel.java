package com.varunu28.scratchpad.oops.dynamicpolymorphism;

public class ConcreteCommunicationChannel implements CommunicationChannel {
    @Override
    public void communicate() {
        System.out.println("Communication channel started within ConcreteCommunicationChannel");
    }
}
