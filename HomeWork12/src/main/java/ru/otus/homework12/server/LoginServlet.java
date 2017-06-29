package ru.otus.homework12.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private static final String LOGIN_PAGE_TEMPLATE = "login_page_template.html";
    private final CacheControlServer server;

    public LoginServlet(CacheControlServer server) {
        this.server = server;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!server.auth(req, resp)){
            resp.getWriter().println(PageBuilder.getInstance().buildPage(LOGIN_PAGE_TEMPLATE, null));
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_FOUND);
        resp.setHeader(CacheControlServer.HEADER_LOCATION, CacheControlServer.ADMIN_PAGE);
    }
}
