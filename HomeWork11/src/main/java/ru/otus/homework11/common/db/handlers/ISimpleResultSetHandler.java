package ru.otus.homework11.common.db.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ISimpleResultSetHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
