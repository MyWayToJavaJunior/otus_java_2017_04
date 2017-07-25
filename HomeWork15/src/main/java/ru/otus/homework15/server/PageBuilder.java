package ru.otus.homework15.server;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class PageBuilder {
    private static final String TEMPLATES_LOCATION = "template_html" + File.separator;

    private static PageBuilder ourInstance = new PageBuilder();

    private final Configuration configuration;

    private PageBuilder() {
        configuration = new Configuration();
    }

    static PageBuilder getInstance() {
        return ourInstance;
    }

    public String buildPage(String pageTemplateName, Map<String, Object> parameters) {
        try (Writer pageTextWriter = new StringWriter()) {
            Template template = configuration.getTemplate(TEMPLATES_LOCATION + pageTemplateName);
            template.process(parameters, pageTextWriter);
            return pageTextWriter.toString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
