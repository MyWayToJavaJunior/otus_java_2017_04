package ru.otus.homework09.db;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.File;

public class DBSettings {
    public static final String MYSQL_SYSTEM_DB = "information_schema";

    private static final int DEFAULT_MYSQL_PORT = 3306;

    private String host;
    private int port = DEFAULT_MYSQL_PORT;
    private String databseName;
    private String login;
    private String password;

    private static DBSettings instance;

    private DBSettings() {
        loadFromXML();
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

    public void loadFromXML() {
        File file = new File(getClass().getClassLoader().getResource("dbsettings.xml").getFile());
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
        private static final String PROPERTY_HOST = "host";
        private static final String PROPERTY_PORT = "port";
        private static final String PROPERTY_DATABASE = "database";
        private static final String PROPERTY_USERNAME = "username";
        private static final String PROPERTY_PASSWORD = "password";

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals(NODE_PROPERTY)) {
                String propertyName = attributes.getValue(ATTR_NAME);
                if (propertyName.equals(PROPERTY_HOST)) {
                    host = attributes.getValue(ATTR_VALUE);
                }
                else if (propertyName.equals(PROPERTY_PORT)) {
                    try {
                        port = Integer.parseInt(attributes.getValue(ATTR_VALUE));
                    } catch (NumberFormatException e){
                        port = DEFAULT_MYSQL_PORT;
                    }
                }
                else if (propertyName.equals(PROPERTY_DATABASE)) {
                    databseName = attributes.getValue(ATTR_VALUE);
                }
                else if (propertyName.equals(PROPERTY_USERNAME)) {
                    login = attributes.getValue(ATTR_VALUE);
                }
                else if (propertyName.equals(PROPERTY_PASSWORD)) {
                    password = attributes.getValue(ATTR_VALUE);
                }
            }
        }
    }

}
