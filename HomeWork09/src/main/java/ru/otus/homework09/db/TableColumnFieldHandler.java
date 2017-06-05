package ru.otus.homework09.db;

import javax.persistence.Column;
import java.lang.reflect.Field;

public interface TableColumnFieldHandler {
    void handle(Object obj, Field field, Column columnAnnotation) throws IllegalAccessException;
}
