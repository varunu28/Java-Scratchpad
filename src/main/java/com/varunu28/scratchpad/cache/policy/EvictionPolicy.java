package com.varunu28.scratchpad.cache.policy;

/*
 * EvictionPolicy dictates how a key is evicted when a com.varunu28.scratchpad.cache reaches its limit.
 * */
public interface EvictionPolicy {

    // Evicts a key based on the policy
    String evict();

    // Records that the key is accessed
    void recordUsage(String key);
}
