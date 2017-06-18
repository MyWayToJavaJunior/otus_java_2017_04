package ru.otus.homework11;

import org.junit.*;
import ru.otus.homework11.common.db.DBSettings;
import ru.otus.homework11.common.db.datasets.UserDataSet;
import ru.otus.homework11.common.db.DatabaseCreator;
import ru.otus.homework11.common.db.IDatabaseService;
import ru.otus.homework11.reflection.orm.ReflectionORMDatabaseService;

import java.sql.SQLException;

public class ReflectionORMDatabaseServiceTest {
    private static final String TEST_USER_NAME = "Валентин Скоробагатько";
    private static final String TEST_USER_NEW_NAME = TEST_USER_NAME + " Взрослый";

    private static final int TEST_USER_AGE = 13;
    private static final int TEST_USER_NEW_AGE = 35;

    private static final DBSettings settings = DBSettings.getInstance();

    @BeforeClass
    public static void createDatabase() {
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();
        DatabaseCreator.createHomework10Database(settings);
        DatabaseCreator.createUsersTable(settings);
    }

    @Before
    public void clearUsersTable() {
        DatabaseCreator.clearUsersTable(settings);
    }

    @Test
    public void insertUserTest01() {
        try(IDatabaseService service = new ReflectionORMDatabaseService(settings.getDefaultDbSettingsXmlFn())) {

            UserDataSet user = new UserDataSet(1L, TEST_USER_AGE, TEST_USER_NAME);
            service.save(user);

            UserDataSet dbuser = service.read(1L);
            Assert.assertTrue(dbuser.equals(user));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void insertUserTest02() {
        try(IDatabaseService service = new ReflectionORMDatabaseService(settings.getDefaultDbSettingsXmlFn())) {

            UserDataSet user = new UserDataSet(null, TEST_USER_AGE, TEST_USER_NAME);
            service.save(user);

            UserDataSet dbuser = service.read(1L);
            Assert.assertTrue(dbuser.getName().equals(user.getName()) && dbuser.getAge() == user.getAge());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void updateUserTest02() {
        try(IDatabaseService service = new ReflectionORMDatabaseService(settings.getDefaultDbSettingsXmlFn())) {

            UserDataSet user = new UserDataSet(null, TEST_USER_AGE, TEST_USER_NAME);
            service.save(user);

            user = service.read(1L);
            user.setAge(TEST_USER_NEW_AGE);
            user.setName(TEST_USER_NEW_NAME);
            service.save(user);

            UserDataSet dbuser = service.read(1L);

            Assert.assertTrue(dbuser.equals(user));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @AfterClass
    public static void dropDatabase() {
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();
        DatabaseCreator.dropHomework10Database(settings);
    }
}
