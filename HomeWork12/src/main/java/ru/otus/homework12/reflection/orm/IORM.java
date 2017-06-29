package ru.otus.homework12.reflection.orm;

import java.sql.SQLException;

public interface IORM {
    <T> T loadObject(Class<T> clazz, IMetaData metaData, long id) throws IllegalAccessException, InstantiationException, SQLException;
    boolean updateObject(Object obj, IMetaData metaData) throws IllegalAccessException, SQLException;
    boolean insertObject(Object obj, IMetaData metaData) throws SQLException, IllegalAccessException;
}
