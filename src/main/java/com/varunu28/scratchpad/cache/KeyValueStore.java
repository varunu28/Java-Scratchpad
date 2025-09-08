package com.varunu28.scratchpad.cache;

import com.varunu28.scratchpad.cache.policy.EvictionPolicy;
import java.util.HashMap;
import java.util.Map;

public class KeyValueStore {

    private final Map<String, String> store;
    private final int CAPACITY;
    private final EvictionPolicy evictionPolicy;

    public KeyValueStore(int capacity, EvictionPolicy evictionPolicy) {
        this.CAPACITY = capacity;
        this.evictionPolicy = evictionPolicy;
        this.store = new HashMap<>();
    }

    public void set(String key, String value) {
        if (!this.store.containsKey(key) && this.store.size() == this.CAPACITY) {
            String evictedKey = this.evictionPolicy.evict();
            this.store.remove(evictedKey);
        }
        this.store.put(key, value);
        this.evictionPolicy.recordUsage(key);
    }

    public String get(String key) {
        if (!this.store.containsKey(key)) {
            return "Key not found";
        }
        this.evictionPolicy.recordUsage(key);
        return this.store.get(key);
    }
}
