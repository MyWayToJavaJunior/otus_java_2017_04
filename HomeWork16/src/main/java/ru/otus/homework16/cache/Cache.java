package ru.otus.homework16.cache;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, V> implements ICache<K, V>{

    public static final long DEFAULT_CACHED_OBJECT_LIFE_TIME = 1000 * 60 * 10;
    public static final long DEFAULT_CACHED_OBJECT_IDLE_TIME = 1000 * 60 * 5;
    public static final int DEFAULT_CACHE_MAX_SIZE = 1000;

    private static final String JSON_PROP_CACHED_OBJECTS_COUNT = "cachedObjectsCount";
    private static final String JSON_PROP_NUMBER_OF_HITS = "numberOfHits";
    private static final String JSON_PROP_NUMBER_OF_MISSES = "numberOfMisses";
    private static final String JSON_PROP_MAXIMAL_LIFE_TIME = "maximalLifeTime";
    private static final String JSON_PROP_MAXIMAL_IDLE_TIME = "maximalIdleTime";
    private static final String JSON_PROP_MAXIMAL_SIZE = "maximalSize";

    @Expose
    private long maximalLifeTime;
    @Expose
    private long maximalIdleTime;
    @Expose
    private int maximalSize;

    @Expose
    private int numberOfHits;
    @Expose
    private int numberOfMisses;

    private boolean timerWorking;

    private final Map<K, SoftReference<Envelope>> cache = new ConcurrentHashMap<>();

    private final Timer timer;

    public Cache(long maximalLifeTime, long maximalIdleTime, int maximalSize) {
        this.maximalLifeTime = maximalLifeTime;
        this.maximalIdleTime = maximalIdleTime;
        this.maximalSize = maximalSize;
        timer = new Timer();
        timerWorking = false;
        restartTimer();
    }

    private void restartTimer() {
        long timerPeriod = Math.min(maximalLifeTime, maximalIdleTime);

        if (timerWorking) {
            timer.cancel();
            timer.purge();
            timerWorking = false;
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeUnused();
            }
        }, 0, timerPeriod);
        timerWorking = true;
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
        else {
            numberOfMisses++;
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

    public long getMaximalLifeTime() {
        return maximalLifeTime;
    }

    public long getMaximalIdleTime() {
        return maximalIdleTime;
    }

    public int getMaximalSize() {
        return maximalSize;
    }

    public void setMaximalLifeTime(long maximalLifeTime) {
        boolean isNewValueGreaterOrEqual = this.maximalLifeTime <= maximalLifeTime;
        this.maximalLifeTime = maximalLifeTime;
        if (!isNewValueGreaterOrEqual) {
            removeUnused();
        }
    }

    public void setMaximalIdleTime(long maximalIdleTime) {
        boolean isNewValueGreaterOrEqual = this.maximalIdleTime <= maximalIdleTime;
        this.maximalIdleTime = maximalIdleTime;
        if (!isNewValueGreaterOrEqual) {
            removeUnused();
        }
    }

    public void setMaximalSize(int maximalSize) {
        this.maximalSize = maximalSize;
        removeUnusedIfMaxSize();
    }

    @Override
    public String toJSONString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(JSON_PROP_CACHED_OBJECTS_COUNT, this.getCachedObjectsCount());
        jsonObject.addProperty(JSON_PROP_NUMBER_OF_HITS, this.getNumberOfHits());
        jsonObject.addProperty(JSON_PROP_NUMBER_OF_MISSES, this.getNumberOfMisses());

        jsonObject.addProperty(JSON_PROP_MAXIMAL_LIFE_TIME, this.getMaximalLifeTime());
        jsonObject.addProperty(JSON_PROP_MAXIMAL_IDLE_TIME, this.getMaximalIdleTime());
        jsonObject.addProperty(JSON_PROP_MAXIMAL_SIZE, this.getMaximalSize());

        return jsonObject.toString();
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
            return System.currentTimeMillis() - creationTime > maximalLifeTime;
        }

        boolean isMustBeRemovedByIdleTime() {
            return System.currentTimeMillis() - lastUsageTime > maximalIdleTime;
        }

        boolean isMustBeRemoved(){
            return isMustBeRemovedByIdleTime() || isMustBeRemovedByLifeTime();
        }

    }
}
