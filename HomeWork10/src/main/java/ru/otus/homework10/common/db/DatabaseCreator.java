package ru.otus.homework10.common.db;

import java.util.Arrays;

public class DatabaseCreator {
    private static final String USERS_TABLE_NAME = "users";
    private static final String FIELD_ID = "ID";
    private static final String FIELD_AGE = "AGE";
    private static final String FIELD_NAME = "NAME";

    private static final String FIELD_ID_TYPE = "BIGINT(20) NOT NULL AUTO_INCREMENT";
    private static final String FIELD_AGE_TYPE = "INT(3) NOT NULL DEFAULT 0";
    private static final String FIELD_NAME_TYPE = "VARCHAR(255) DEFAULT NULL";

    private DatabaseCreator() {

    }

    public static boolean createHomework10Database(DBSettings settings){
        boolean res = false;
        if (!DatabaseHelper.createDatabase(settings)) {
            return res;
        }

        return true;
    }

    public static boolean createUsersTable(DBSettings settings) {
        return DatabaseHelper.createTable(settings, USERS_TABLE_NAME,
                Arrays.asList(FIELD_ID, FIELD_AGE, FIELD_NAME),
                Arrays.asList(FIELD_ID_TYPE, FIELD_AGE_TYPE, FIELD_NAME_TYPE),
                Arrays.asList(FIELD_ID)
                );
    }

    public static boolean dropHomework10Database(DBSettings settings) {
        return DatabaseHelper.dropDatabase(settings);
    }

    public static boolean clearUsersTable(DBSettings settings) {
        return DatabaseHelper.clearTable(settings, USERS_TABLE_NAME);
    }
}
