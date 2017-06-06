package ru.otus.homework09;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.homework09.dao.DatabaseCreator;
import ru.otus.homework09.db.DBSettings;

public class DatabaseCreatorTest {
    DBSettings settings = DBSettings.getInstance();

    @Test
    public void databaseCreationTest() {
        Assert.assertTrue("Database creation filed", DatabaseCreator.createHomework09Database(settings));
        Assert.assertTrue("Database drop filed", DatabaseCreator.dropHomework09Database(settings));
    }
}
