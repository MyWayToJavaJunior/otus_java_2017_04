package ru.otus.homework09.db;

import java.sql.SQLException;

public interface IORM {
    <T> T loadObject(Class<T> clazz, long id) throws IllegalAccessException, InstantiationException, SQLException;
    boolean updateObject(Object obj) throws IllegalAccessException, SQLException;
    boolean insertObject(Object obj) throws SQLException, IllegalAccessException;
}
