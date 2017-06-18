package ru.otus.homework11;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.homework11.cache.Cache;

public class CacheTest {
    private static final String TEST_STR_TEMPLATE = "Test string #%d";
    private static final String FIRST_STRING_NOT_REMOVED = "First string not removed";
    private static final String SECOND_STRING_NOT_EQUALS_CACHED_STRING = "Second string not equals cached string";
    private static final String INVALID_CACHED_OBJECTS_COUNT = "Invalid cached objects count";

    @Test
    public void simpleTest() {
        try(Cache<Integer, String> cache = new Cache<>(Cache.DEFAULT_CACHED_OBJECT_LIFE_TIME, Cache.DEFAULT_CACHED_OBJECT_IDLE_TIME, Cache.DEFAULT_CACHE_MAX_SIZE)) {
            String source = String.format(TEST_STR_TEMPLATE, 0);
            cache.put(0, source);
            String dest = cache.get(0);

            Assert.assertTrue(source.equals(dest));
        }
    }

    @Test
    public void removeByLifeTimeTest() {
        final long cachedObjectLifTime = 1000;
        try (Cache<Integer, String> cache = new Cache<>(cachedObjectLifTime, Cache.DEFAULT_CACHED_OBJECT_IDLE_TIME, Cache.DEFAULT_CACHE_MAX_SIZE)) {

            // Добавление первой строки
            int id = 0;
            String testStr = String.format(TEST_STR_TEMPLATE, id);
            cache.put(id, testStr);

            // Ожидание примерно 2х проходов таймера
            hold(cachedObjectLifTime * 2);

            // Добавление второй строки после ожидания
            id++;
            testStr = String.format(TEST_STR_TEMPLATE, id);
            cache.put(id, testStr);

            // Первая строка должна уже точно уйти из кэша
            id = 0;
            String cachedStr = cache.get(id);
            Assert.assertTrue(FIRST_STRING_NOT_REMOVED, cachedStr == null);

            id++;
            cachedStr = cache.get(id);
            Assert.assertTrue(SECOND_STRING_NOT_EQUALS_CACHED_STRING, testStr.equals(cachedStr));

            // Ожидание примерно 2х проходов таймера
            hold(cachedObjectLifTime * 3);


            // К этому времени кэш должен быть точно пуст
            Assert.assertEquals(INVALID_CACHED_OBJECTS_COUNT, 0, cache.getCachedObjectsCount());
        }
    }

    @Test
    public void removeByIdleTimeTest() {
        final long cachedObjectIdleTime = 1000;
        try (Cache<Integer, String> cache = new Cache<>(Cache.DEFAULT_CACHED_OBJECT_LIFE_TIME, cachedObjectIdleTime, Cache.DEFAULT_CACHE_MAX_SIZE)) {

            // Добавление первой строки
            int id = 0;
            String testStr = String.format(TEST_STR_TEMPLATE, id);
            cache.put(id, testStr);

            // Ожидание примерно 2х проходов таймера
            hold(cachedObjectIdleTime * 2);

            // Добавление второй строки после ожидания
            id++;
            testStr = String.format(TEST_STR_TEMPLATE, id);
            cache.put(id, testStr);

            // Первая строка должна уже точно уйти из кэша
            id = 0;
            String cachedStr = cache.get(id);
            Assert.assertTrue(FIRST_STRING_NOT_REMOVED, cachedStr == null);

            // Обновление времени последнего использования для воторой строки (+ cachedObjectIdleTime)
            id++;
            cachedStr = cache.get(id);
            Assert.assertTrue(SECOND_STRING_NOT_EQUALS_CACHED_STRING, testStr.equals(cachedStr));

            // Ожидание одного прохода таймера
            hold(cachedObjectIdleTime);

            // Воторая строка должна быть еще в кэше
            Assert.assertEquals(INVALID_CACHED_OBJECTS_COUNT + "# 1", 1, cache.getCachedObjectsCount());

            // Ожидание примерно 2х проходов таймера
            hold(cachedObjectIdleTime * 2);

            // К этому времени кэш должен быть точно пуст
            Assert.assertEquals(INVALID_CACHED_OBJECTS_COUNT + "# 2", 0, cache.getCachedObjectsCount());
        }
    }

    private void hold(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ignored) {
        }
    }

}
