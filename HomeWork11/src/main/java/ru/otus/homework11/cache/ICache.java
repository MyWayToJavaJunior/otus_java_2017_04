package ru.otus.homework11.cache;

public interface ICache<K, V> extends AutoCloseable{
    void put(K key, V value);
    V get(K key);
}
