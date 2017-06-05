package ru.otus.homework09.db;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.File;

public class DBSettings {
    public static final String MYSQL_SYSTEM_DB = "information_schema";

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_MYSQL_PORT = 3306;

    private static final String DEFAULT_DATABASE_NAME = "homework09";
    private static final String DEFAULT_LOGIN = "anyuser";
    private static final String DEFAULT_PASSWORD = "anyuser3310";

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_MYSQL_PORT;
    private String databseName = DEFAULT_DATABASE_NAME;
    private String login = DEFAULT_LOGIN;
    private String password = DEFAULT_PASSWORD;

    private static DBSettings instance;

    private DBSettings() {
        loadFromPersistenceXML();
    }

    public static DBSettings getInstance() {
        if (instance == null) {
            instance = new DBSettings();
        }
        return instance;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabseName() {
        return databseName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "DBSettings{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", databseName='" + databseName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    private void loadFromPersistenceXML() {
        File file = new File(getClass().getClassLoader().getResource("META-INF/persistence.xml").getFile());
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, new DBSettingsHandler());

        } catch (Exception e){
            System.err.println(e.getMessage());
        }

    }

    class DBSettingsHandler extends DefaultHandler {

        private static final String ATTR_VALUE = "value";
        private static final String ATTR_NAME = "name";
        private static final String NODE_PROPERTY = "property";
        private static final String PROPERTY_USERNAME = "hibernate.connection.username";
        private static final String PROPERTY_PASSWORD = "hibernate.connection.password";
        private static final String PROPERTY_URL = "hibernate.connection.url";

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals(NODE_PROPERTY)) {
                String propertyName = attributes.getValue(ATTR_NAME);
                if (propertyName.equals(PROPERTY_USERNAME)) {
                    login = attributes.getValue(ATTR_VALUE);
                } else if (propertyName.equals(PROPERTY_PASSWORD)) {
                    password = attributes.getValue(ATTR_VALUE);
                } else if (propertyName.equals(PROPERTY_URL)) {
                    String[] url = attributes.getValue(ATTR_VALUE).split("/");
                    String[] server = url[2].split(":");
                    host = server[0];
                    port = (server.length > 1)? Integer.parseInt(server[1]): DEFAULT_MYSQL_PORT;
                    databseName = url[3];
                }
            }
        }
    }

}
