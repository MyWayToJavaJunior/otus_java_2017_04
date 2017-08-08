package ru.otus.homework16.webserver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminServlet extends HttpServlet{
    private static final String ADMIN_PAGE_TEMPLATE = "admin_page_template.html";

    private final CacheControlServer server;

    public AdminServlet(CacheControlServer server) {
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

        resp.getWriter().println(PageBuilder.getInstance().buildPage(ADMIN_PAGE_TEMPLATE, null));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
