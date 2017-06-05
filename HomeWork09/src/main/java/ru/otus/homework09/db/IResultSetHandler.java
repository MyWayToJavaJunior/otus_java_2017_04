package ru.otus.homework09.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IResultSetHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
