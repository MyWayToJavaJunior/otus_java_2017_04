package ru.otus.homework11.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, V> implements ICache<K, V>{
    public static final long DEFAULT_CACHED_OBJECT_LIFE_TIME = 1000 * 60 * 10;
    public static final long DEFAULT_CACHED_OBJECT_IDLE_TIME = 1000 * 60 * 5;
    public static final int DEFAULT_CACHE_MAX_SIZE = 1000;

    private final long maximalLifeTime;
    private final long maximalIdleTime;
    private final int maximalSize;

    private int numberOfHits;
    private int numberOfMisses;

    private final Map<K, SoftReference<Envelope>> cache = new ConcurrentHashMap<>();

    private final Timer timer;

    public Cache(long maximalLifeTime, long maximalIdleTime, int maximalSize) {
        this.maximalLifeTime = maximalLifeTime;
        this.maximalIdleTime = maximalIdleTime;
        this.maximalSize = maximalSize;
        long timerPeriod = Math.min(maximalLifeTime, maximalIdleTime);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeUnused();
            }
        }, 0, timerPeriod);
    }

    @Override
    public void put(K key, V value) {
        if (value == null) return;

        if (!removeUnusedIfMaxSize() && !cache.containsKey(key)) {
            return;
        }

        Envelope envelope = new Envelope(value);
        SoftReference<Envelope> reference = new SoftReference<>(envelope);
        cache.put(key, reference);
    }

    @Override
    public V get(K key) {
        V value = null;
        SoftReference<Envelope> reference = cache.get(key);
        if (reference != null) {
            Envelope envelope = reference.get();
            if (envelope != null) {
                value = envelope.getValue();
                numberOfHits++;
            }
            else {
                numberOfMisses++;
            }
        }
        return value;
    }


    private void removeUnused() {
        cache.entrySet().removeIf(entry -> entry.getValue().get() != null && entry.getValue().get().isMustBeRemoved());
    }

    private boolean removeUnusedIfMaxSize() {
        if (cache.size() >= maximalSize) {
            removeUnused();
        }

        return cache.size() < maximalSize;
    }

    public int getNumberOfHits() {
        return numberOfHits;
    }

    public int getNumberOfMisses() {
        return numberOfMisses;
    }

    public int getCachedObjectsCount(){
        return cache.size();
    }

    @Override
    public void close() {
        timer.cancel();
        timer.purge();
    }

    private class Envelope {
        private V value;
        private final long creationTime;
        private long lastUsageTime;

        Envelope(V value) {
            this.value = value;
            creationTime = System.currentTimeMillis();
            lastUsageTime = creationTime;
        }

        V getValue() {
            lastUsageTime = System.currentTimeMillis();
            return value;
        }

        boolean isMustBeRemovedByLifeTime() {
            boolean res = System.currentTimeMillis() - creationTime > maximalLifeTime;
            return res;
        }

        boolean isMustBeRemovedByIdleTime() {
            boolean res = System.currentTimeMillis() - lastUsageTime > maximalIdleTime;
            return res;
        }

        boolean isMustBeRemoved(){
            return isMustBeRemovedByIdleTime() || isMustBeRemovedByLifeTime();
        }

    }
}
