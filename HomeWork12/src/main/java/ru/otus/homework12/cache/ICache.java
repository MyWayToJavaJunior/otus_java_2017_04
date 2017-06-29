package ru.otus.homework12.cache;

public interface ICache<K, V> extends AutoCloseable{
    void put(K key, V value);
    V get(K key);

    int getNumberOfHits();
    int getNumberOfMisses();
    int getCachedObjectsCount();

    long getMaximalLifeTime();
    long getMaximalIdleTime();
    int getMaximalSize();

    void setMaximalLifeTime(long maximalLifeTime);
    void setMaximalIdleTime(long maximalIdleTime);
    void setMaximalSize(int maximalSize);
}
