package ru.otus.homework12.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.homework12.cache.ICache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private final static String PUBLIC_HTML = "public_html";

    private Server server;
    private Map<String, String> registeredUsers;
    private ICache cacheToControl;

    public CacheControlServer(ICache cacheToControl) {
        this.cacheToControl = cacheToControl;

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);
        resourceHandler.setDirAllowed(false);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new AdminServlet(this, cacheToControl)), ADMIN_PAGE);
        context.addServlet(new ServletHolder(new LoginServlet(this)), LOGIN_PAGE);


        server = new Server(PORT);

        server.setHandler(new HandlerList(resourceHandler, context));

        registeredUsers = new HashMap<>();
    }

    public void stopServer(){
        try {
            server.stop();
            while (!server.isStopped()) {
                Thread.sleep(10);
            }
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

    boolean auth(HttpServletRequest req, HttpServletResponse resp) {
        String sessionID = req.getSession().getId();
        String sessionDigest = (String) req.getSession().getAttribute(SESSION_DGEST_KEY);
        if (sessionDigest != null){
            return registeredUsers.get(sessionID).equals(sessionDigest);
        }
        else {
            return register(req, resp);
        }
    }

    private boolean register(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String passw = req.getParameter("passw");

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
                byte[] digest = MessageDigest.getInstance("MD5").digest((login + ":" + passw).getBytes());

                StringBuffer hexString = new StringBuffer();

                for (int i = 0; i < digest.length; i++) {
                    if ((0xff & digest[i]) < 0x10) {
                        hexString.append("0"
                                + Integer.toHexString((0xFF & digest[i])));
                    } else {
                        hexString.append(Integer.toHexString(0xFF & digest[i]));
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
