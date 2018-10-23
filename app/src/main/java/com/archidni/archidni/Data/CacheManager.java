package com.archidni.archidni.Data;

public interface CacheManager<U,V> {
    V get (U key);
    void set (U key,V value);
    void remove (U key);
    void clear ();
}
