package com.varunu28.scratchpad.oops.dynamicpolymorphism;

public class DynamicPolymorphismDemo {

    static void main() {
        CommunicationChannel extendedCommunicationChannel = new ExtendedCommunicationChannel();
        CommunicationChannel concreteCommunicationChannel = new ConcreteCommunicationChannel();

        extendedCommunicationChannel.communicate();
        concreteCommunicationChannel.communicate();
    }
}
