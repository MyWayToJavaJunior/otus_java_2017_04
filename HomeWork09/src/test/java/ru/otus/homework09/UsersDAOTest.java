package ru.otus.homework09;

import org.junit.*;
import ru.otus.homework09.dao.DatabaseCreator;
import ru.otus.homework09.dao.UsersDAO;
import ru.otus.homework09.dao.UsersDataSet;
import ru.otus.homework09.db.DBSettings;
import ru.otus.homework09.db.helpers.ConnectionHelper;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAOTest {
    private static final String TEST_USER_NAME = "Валентин Скоробагатько";
    private static final String TEST_USER_NEW_NAME = TEST_USER_NAME + " Взрослый";

    private static final int TEST_USER_AGE = 13;
    private static final int TEST_USER_NEW_AGE = 35;

    private static final DBSettings settings = DBSettings.getInstance();

    @BeforeClass
    public static void createDatabase() {
        DatabaseCreator.createHomework09Database(DBSettings.getInstance());
    }

    @Before
    public void clearUsersTable() {
        DatabaseCreator.clearUsersTable(settings);
    }

    @Test
    public void insertUserTest01() {
        try (Connection connection = ConnectionHelper.getLocalConnection(settings.getDatabseName(), settings.getLogin(), settings.getPassword())) {
            UsersDAO dao = new UsersDAO(connection);
            UsersDataSet user = new UsersDataSet(1L, TEST_USER_AGE, TEST_USER_NAME);
            dao.updateUser(user);

            UsersDataSet dbuser = dao.getUser(1L);
            Assert.assertTrue(dbuser.equals(user));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void insertUserTest02() {
        try (Connection connection = ConnectionHelper.getLocalConnection(settings.getDatabseName(), settings.getLogin(), settings.getPassword())) {
            UsersDAO dao = new UsersDAO(connection);
            UsersDataSet user = new UsersDataSet(null, TEST_USER_AGE, TEST_USER_NAME);
            dao.updateUser(user);

            UsersDataSet dbuser = dao.getUser(1L);
            Assert.assertTrue(dbuser.getName().equals(user.getName()) && dbuser.getAge() == user.getAge());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void updateUserTest02() {
        try (Connection connection = ConnectionHelper.getLocalConnection(settings.getDatabseName(), settings.getLogin(), settings.getPassword())) {
            UsersDAO dao = new UsersDAO(connection);
            UsersDataSet user = new UsersDataSet(null, TEST_USER_AGE, TEST_USER_NAME);
            dao.updateUser(user);

            user = dao.getUser(1L);
            user.setAge(TEST_USER_NEW_AGE);
            user.setName(TEST_USER_NEW_NAME);
            dao.updateUser(user);

            UsersDataSet dbuser = dao.getUser(1L);

            Assert.assertTrue(dbuser.equals(user));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @AfterClass
    public static void dropDatabase() {
        DatabaseCreator.dropHomework09Database(DBSettings.getInstance());
    }
}
