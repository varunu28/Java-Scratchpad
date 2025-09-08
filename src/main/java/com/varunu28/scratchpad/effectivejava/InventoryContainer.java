package com.varunu28.scratchpad.effectivejava;

public class InventoryContainer {

    private InventoryContainer() {
    }

    public static InventoryContainer getInventoryContainer(int size) {
        return size > 100 ? new BigInventoryContainer(size) : new SmallInventoryContainer(size);
    }

    private static class SmallInventoryContainer extends InventoryContainer {
        private final int[] container;

        public SmallInventoryContainer(int size) {
            this.container = new int[size];
        }
    }

    private static class BigInventoryContainer extends InventoryContainer {
        private final long[] container;

        public BigInventoryContainer(int size) {
            this.container = new long[size];
        }
    }
}
