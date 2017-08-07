package ru.otus.homework16.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.homework16.cache.ICache;
import ru.otus.homework16.server.websocket.AdminPageDataWebSocketServlet;
import ru.otus.homework16.server.websocket.WebsocketRequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CacheControlServer {
    private final static int PORT = 8090;

    static final String HEADER_LOCATION = "Location";

    private static final String SESSION_DGEST_KEY = "SESSION_DGEST_KEY";

    static final String LOGIN_PAGE = "/login";
    static final String ADMIN_PAGE = "/admin";
    private static final String ADMIN_PAGE_DATA = "/admin_page_data";

    private final static String PUBLIC_HTML = "public_html";
    public static final String REQUEST_PARAM_LOGIN = "login";
    public static final String REQUEST_PARAM_PASSWORD = "passw";
    public static final String ALGORITHM_MD5 = "MD5";

    private Server server;
    private Map<String, String> registeredUsers;

    public CacheControlServer(ICache cacheToControl, WebsocketRequestService websocketRequestService) {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);
        resourceHandler.setDirAllowed(false);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new AdminServlet(this, cacheToControl)), ADMIN_PAGE);
        context.addServlet(new ServletHolder(new LoginServlet(this)), LOGIN_PAGE);
        context.addServlet(new ServletHolder(new AdminPageDataWebSocketServlet(websocketRequestService)), ADMIN_PAGE_DATA);


        server = new Server(PORT);

        server.setHandler(new HandlerList(resourceHandler, context));

        registeredUsers = new HashMap<>();
    }

    public void stopServer(){
        try {
            server.stop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void startServer() throws Exception {
        server.start();
    }

    public void join() throws InterruptedException {
        server.join();
    }

    boolean auth(HttpServletRequest req) {
        String sessionID = req.getSession().getId();
        String sessionDigest = (String) req.getSession().getAttribute(SESSION_DGEST_KEY);
        if (sessionDigest != null){
            return registeredUsers.get(sessionID).equals(sessionDigest);
        }
        else {
            return register(req);
        }
    }

    private boolean register(HttpServletRequest req) {
        String login = req.getParameter(REQUEST_PARAM_LOGIN);
        String passw = req.getParameter(REQUEST_PARAM_PASSWORD);

        String digest = getDigest(login, passw);
        if (digest != null) {
            req.getSession().setAttribute(SESSION_DGEST_KEY, digest);
            registeredUsers.put(req.getSession().getId(), digest);
        }
        return digest != null;
    }

    private static String getDigest(String login, String passw) {
        if (login != null && passw != null) {
            try {
                byte[] digest = MessageDigest.getInstance(ALGORITHM_MD5).digest((login + ":" + passw).getBytes());

                StringBuilder hexString = new StringBuilder();

                for (byte aDigest : digest) {
                    if ((0xff & aDigest) < 0x10) {
                        hexString.append("0").append(Integer.toHexString((0xFF & aDigest)));
                    } else {
                        hexString.append(Integer.toHexString(0xFF & aDigest));
                    }
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
