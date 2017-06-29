package ru.otus.homework12;

import org.junit.*;
import ru.otus.homework12.cache.Cache;
import ru.otus.homework12.cache.ICache;
import ru.otus.homework12.server.AdminServlet;
import ru.otus.homework12.server.CacheControlServer;

import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CacheControlServerTest {

    private static final String SERVER_ADDRESS = "http://127.0.0.1:8090/";
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String INDEX_PAGE_REQUEST = "index.html";
    private static final String LOGIN_PAGE_REQUEST = "login";
    private static final String ADMIN_PAGE_REQUEST = "admin";
    private static final String DEFAULT_LOGIN_PARAMS = "login=login&passw=passw";

    @Test
    public void responseCodeTest() {
        System.out.println("ResponseCodeTest - begin");

        ICache<Long, String> cache = createCahe();
        CacheControlServer server = new CacheControlServer(cache);

        try {
            server.startServer();
            HttpURLConnection connection;

            connection = sendRequest(SERVER_ADDRESS + INDEX_PAGE_REQUEST, GET_METHOD, null);
            Assert.assertNotNull(connection);
            Assert.assertEquals(HttpServletResponse.SC_OK, connection.getResponseCode());

            connection = sendRequest(SERVER_ADDRESS + LOGIN_PAGE_REQUEST, GET_METHOD, null);
            Assert.assertNotNull(connection);
            Assert.assertEquals(HttpServletResponse.SC_OK, connection.getResponseCode());

            connection = sendRequest(SERVER_ADDRESS + ADMIN_PAGE_REQUEST, GET_METHOD, null);
            Assert.assertNotNull(connection);
            Assert.assertEquals(HttpServletResponse.SC_FOUND, connection.getResponseCode());

            connection = sendRequest(SERVER_ADDRESS + LOGIN_PAGE_REQUEST, POST_METHOD, DEFAULT_LOGIN_PARAMS);
            Assert.assertNotNull(connection);
            Assert.assertEquals(HttpServletResponse.SC_FOUND, connection.getResponseCode());

            server.stopServer();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("ResponseCodeTest - end");
    }

    @Test
    public void changesCacheParamsTest() {
        System.out.println("ChangesCacheParamsTest - begin");

        final long NEW_CACHED_OBJECT_LIFE_TIME = Cache.DEFAULT_CACHED_OBJECT_LIFE_TIME * 2;
        final long NEW_CACHED_OBJECT_IDLE_TIME = Cache.DEFAULT_CACHED_OBJECT_IDLE_TIME * 2;
        final int NEW_CACHE_MAX_SIZE = Cache.DEFAULT_CACHE_MAX_SIZE * 2;

        ICache<Long, String> cache = createCahe();
        CacheControlServer server = new CacheControlServer(cache);

        try {
            server.startServer();
            String params = DEFAULT_LOGIN_PARAMS + "&" +
                    AdminServlet.VARIABLE_MAXIMAL_LIFE_TIME + "=" + NEW_CACHED_OBJECT_LIFE_TIME + "&" +
                    AdminServlet.VARIABLE_MAXIMAL_IDLE_TIME + "=" + NEW_CACHED_OBJECT_IDLE_TIME + "&" +
                    AdminServlet.VARIABLE_MAXIMAL_SIZE + "=" + NEW_CACHE_MAX_SIZE;

            HttpURLConnection connection = sendRequest(SERVER_ADDRESS + ADMIN_PAGE_REQUEST, GET_METHOD, params);
            Assert.assertNotNull(connection);
            Assert.assertEquals(HttpServletResponse.SC_OK, connection.getResponseCode());

            Assert.assertEquals(NEW_CACHED_OBJECT_LIFE_TIME, cache.getMaximalLifeTime());
            Assert.assertEquals(NEW_CACHED_OBJECT_IDLE_TIME, cache.getMaximalIdleTime());
            Assert.assertEquals(NEW_CACHE_MAX_SIZE, cache.getMaximalSize());
            server.stopServer();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println("ChangesCacheParamsTest - end");
    }

    private static ICache<Long, String> createCahe() {
        Cache<Long, String> cache = new Cache<>(Cache.DEFAULT_CACHED_OBJECT_LIFE_TIME, Cache.DEFAULT_CACHED_OBJECT_IDLE_TIME, Cache.DEFAULT_CACHE_MAX_SIZE);
        for (long i = 1; i <= 5; i++) {
            if (i < 4) {
                cache.put(i, "Obj" + i);
            }
            cache.get(i);
        }
        return cache;
    }

    private static HttpURLConnection sendRequest(String sUrl, String method, String parameters){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(sUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setInstanceFollowRedirects(false);

            if (parameters != null) {
                conn.setDoOutput(true);
                try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.writeBytes(parameters);
                    wr.flush();
                }
            }
            conn.connect();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return conn;
    }

}
