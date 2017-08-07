package ru.otus.homework16.common.db.sql;

public class CreateTableSQLBuilder {
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s(%s, primary key (%s))";
    private String tableName;
    private StringBuilder primaryKey;
    private StringBuilder fields;

    public CreateTableSQLBuilder(String tableName) {
        fields = new StringBuilder();
        primaryKey = new StringBuilder();
        this.tableName = tableName;
    }

    public CreateTableSQLBuilder addField(String name, String type, boolean isInPrimaryKey) {
        if (fields.length() != 0) fields.append(", ");
        fields.append(String.format("%s %s", name, type));

        if (isInPrimaryKey) {
            if (primaryKey.length() != 0) primaryKey.append(", ");
            primaryKey.append(name);
        }

        return this;
    }

    public String build() {
        return String.format(CREATE_TABLE_SQL, tableName, fields.toString(), primaryKey);
    }
}
