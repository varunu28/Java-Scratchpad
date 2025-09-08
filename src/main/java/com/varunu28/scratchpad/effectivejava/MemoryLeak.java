package com.varunu28.scratchpad.effectivejava;

import java.util.Arrays;

public class MemoryLeak {
    private ExpensiveObject[] expensiveObjects;
    private int idx;

    public MemoryLeak() {
        this.expensiveObjects = new ExpensiveObject[/* initialCapacity= */ 4];
        this.idx = 0;
    }

    public void add(ExpensiveObject expensiveObject) {
        this.expensiveObjects[idx++] = expensiveObject;
    }

    public ExpensiveObject deleteLastEntry() {
        ExpensiveObject returnObject = this.expensiveObjects[idx];
        this.expensiveObjects[idx--] = null;
        return returnObject;
    }

    private void resize() {
        this.expensiveObjects = Arrays.copyOf(this.expensiveObjects, this.expensiveObjects.length * 2);
    }
}

class ExpensiveObject {
}