package ru.otus.homework13.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.homework13.cache.ICache;
import ru.otus.homework13.common.db.IDatabaseService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet{
    private static final String ADMIN_PAGE_TEMPLATE = "admin_page_template.html";

    private static final String VARIABLE_CACHED_OBJECTS_COUNT = "cachedObjectsCount";
    private static final String VARIABLE_NUMBER_OF_HITS = "numberOfHits";
    private static final String VARIABLE_NUMBER_OF_MISSES = "numberOfMisses";

    private static final String VARIABLE_MAXIMAL_LIFE_TIME = "maximalLifeTime";
    private static final String VARIABLE_MAXIMAL_IDLE_TIME = "maximalIdleTime";
    private static final String VARIABLE_MAXIMAL_SIZE = "maximalSize";

    @Autowired
    private UsersRegistrator registrator;

    @Autowired
    private IDatabaseService dbService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!registrator.auth(req)){
            resp.setStatus(HttpServletResponse.SC_FOUND);
            resp.setHeader(UsersRegistrator.HEADER_LOCATION, "/login");
            return;
        }
        changeCacheParameters(dbService.getCache(), req);

        Map<String, Object> pageVariables = createPageVariablesMap(dbService.getCache());
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
