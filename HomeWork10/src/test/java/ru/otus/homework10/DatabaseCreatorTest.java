package ru.otus.homework10;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.homework10.common.DBSettings;
import ru.otus.homework10.common.db.DatabaseCreator;

public class DatabaseCreatorTest {
    DBSettings settings = DBSettings.getInstance();

    @Test
    public void databaseCreationTest() {
        settings.loadFromDefaultXMLFile();
        Assert.assertTrue("Database creation filed", DatabaseCreator.createHomework10Database(settings));
        Assert.assertTrue("Database drop filed", DatabaseCreator.dropHomework10Database(settings));
    }
}
