package ru.otus.homework16.common.db.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IORMResultSetHandler<T> {
    T handle(Class clazz, ResultSet resultSet) throws SQLException, IllegalAccessException, InstantiationException;
}
