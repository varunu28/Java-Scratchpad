package cache;

import cache.policy.LRUEvictionPolicy;
import cache.policy.MRUEvictionPolicy;

public class CacheRunner {

    public static void main(String[] args) {
        demoCache(new KeyValueStore(3, new LRUEvictionPolicy()));
        demoCache(new KeyValueStore(3, new MRUEvictionPolicy()));
    }

    private static void demoCache(KeyValueStore keyValueStore) {
        keyValueStore.set("a", "1");
        keyValueStore.set("b", "2");
        keyValueStore.set("c", "3"); // Capacity full

        // Cache movement
        System.out.println("Value of a: " + keyValueStore.get("a"));

        // Cache full -> Eviction
        keyValueStore.set("d", "4");
    }
}
