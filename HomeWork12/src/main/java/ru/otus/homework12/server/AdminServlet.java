package ru.otus.homework12.server;

import ru.otus.homework12.cache.ICache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet{
    private static final String ADMIN_PAGE_TEMPLATE = "admin_page_template.html";

    public static final String VARIABLE_CACHED_OBJECTS_COUNT = "cachedObjectsCount";
    public static final String VARIABLE_NUMBER_OF_HITS = "numberOfHits";
    public static final String VARIABLE_NUMBER_OF_MISSES = "numberOfMisses";

    public static final String VARIABLE_MAXIMAL_LIFE_TIME = "maximalLifeTime";
    public static final String VARIABLE_MAXIMAL_IDLE_TIME = "maximalIdleTime";
    public static final String VARIABLE_MAXIMAL_SIZE = "maximalSize";

    private final ICache cacheToControl;
    private final CacheControlServer server;

    public AdminServlet(CacheControlServer server, ICache cacheToControl) {
        this.cacheToControl = cacheToControl;
        this.server = server;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!server.auth(req)){
            resp.setStatus(HttpServletResponse.SC_FOUND);
            resp.setHeader(CacheControlServer.HEADER_LOCATION, CacheControlServer.LOGIN_PAGE);
            return;
        }
        changeCacheParameters(cacheToControl, req);

        Map<String, Object> pageVariables = createPageVariablesMap(cacheToControl);
        resp.getWriter().println(PageBuilder.getInstance().buildPage(ADMIN_PAGE_TEMPLATE, pageVariables));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static Map<String, Object> createPageVariablesMap(ICache cacheToControl){
        Map<String, Object> variables = new HashMap<>();
        if (cacheToControl == null) return variables;

        variables.put(VARIABLE_CACHED_OBJECTS_COUNT, cacheToControl.getCachedObjectsCount());
        variables.put(VARIABLE_NUMBER_OF_HITS, cacheToControl.getNumberOfHits());
        variables.put(VARIABLE_NUMBER_OF_MISSES, cacheToControl.getNumberOfMisses());

        variables.put(VARIABLE_MAXIMAL_LIFE_TIME, cacheToControl.getMaximalLifeTime());
        variables.put(VARIABLE_MAXIMAL_IDLE_TIME, cacheToControl.getMaximalIdleTime());
        variables.put(VARIABLE_MAXIMAL_SIZE, cacheToControl.getMaximalSize());
        return variables;
    }

    private static void changeCacheParameters(ICache cacheToControl, HttpServletRequest req) {
        String maximalLifeTime = req.getParameter(VARIABLE_MAXIMAL_LIFE_TIME);
        String maximalIdleTime = req.getParameter(VARIABLE_MAXIMAL_IDLE_TIME);
        String maximalSize = req.getParameter(VARIABLE_MAXIMAL_SIZE);

        if (maximalLifeTime != null && maximalIdleTime != null && maximalSize != null) {
            cacheToControl.setMaximalLifeTime(Long.parseLong(maximalLifeTime));
            cacheToControl.setMaximalIdleTime(Long.parseLong(maximalIdleTime));
            cacheToControl.setMaximalSize(Integer.parseInt(maximalSize));
        }
    }

}
