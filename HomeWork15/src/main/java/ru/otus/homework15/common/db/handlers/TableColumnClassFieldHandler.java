package ru.otus.homework15.common.db.handlers;

import javax.persistence.Column;
import java.lang.reflect.Field;

public interface TableColumnClassFieldHandler {
    void handle(Field field, Column columnAnnotation);
}
