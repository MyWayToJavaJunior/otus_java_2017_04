package ru.otus.homework10;

import org.junit.*;
import ru.otus.homework10.common.db.datasets.AddressDataSet;
import ru.otus.homework10.common.db.DBSettings;
import ru.otus.homework10.common.db.datasets.PhoneDataSet;
import ru.otus.homework10.common.db.datasets.UserDataSet;
import ru.otus.homework10.common.db.DatabaseCreator;
import ru.otus.homework10.common.db.IDatabaseService;
import ru.otus.homework10.hibernate.HibernateDatabaseService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HibernateDatabaseServiceTest {
    private static final String TEST_USER_NAME = "Валентин Скоробагатько";
    private static final String TEST_USER_NEW_NAME = TEST_USER_NAME + " Взрослый";

    private static final int TEST_USER_AGE = 13;
    private static final int TEST_USER_NEW_AGE = 35;

    private static final DBSettings settings = DBSettings.getInstance();
    private static final String TEST_USER_STREET = "Trafalgar square";
    private static final int TEST_USER_MAIL_INDEX = 17;

    private static UserDataSet buildDefautlUser(Long id) {
        UserDataSet user = new UserDataSet(id, TEST_USER_AGE, TEST_USER_NAME);
        user.setAddress(new AddressDataSet(TEST_USER_STREET, TEST_USER_MAIL_INDEX));
        List<PhoneDataSet> phones = new ArrayList<>();
        phones.add(new PhoneDataSet(8452, "33-33-33"));
        phones.add(new PhoneDataSet(8453, "75-53-53"));
        user.setPhones(phones);
        return user;
    }

    @BeforeClass
    public static void createDatabase() {
        DBSettings settings = DBSettings.getInstance();
        settings.loadFromDefaultXMLFile();
        DatabaseCreator.createHomework10Database(settings);
    }

    @Test
    public void insertUserTest01() {
        try(IDatabaseService service = new HibernateDatabaseService(settings.getDefaultDbSettingsXmlFn())) {

            UserDataSet user = buildDefautlUser(1L);
            service.save(user);

            UserDataSet dber = service.read(1L);
            Assert.assertTrue(dber.equals(user));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void insertUserTest02() {
        try(IDatabaseService service = new HibernateDatabaseService(settings.getDefaultDbSettingsXmlFn())) {

            UserDataSet user = buildDefautlUser(null);
            service.save(user);

            UserDataSet dbuser = service.read(1L);
            Assert.assertTrue(dbuser.getName().equals(user.getName()) && dbuser.getAge() == user.getAge());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void updateUserTest02() {
        try(IDatabaseService service = new HibernateDatabaseService(settings.getDefaultDbSettingsXmlFn())) {

            UserDataSet user = buildDefautlUser(null);
            service.save(user);


            UserDataSet dbuser1 = service.read(1L);
            dbuser1.setAge(TEST_USER_NEW_AGE);
            dbuser1.setName(TEST_USER_NEW_NAME);
            service.save(dbuser1);

            UserDataSet dbuser2 = service.read(1L);

            Assert.assertTrue(dbuser2.equals(dbuser1));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void selectUsersTest() {
        try(IDatabaseService service = new HibernateDatabaseService(settings.getDefaultDbSettingsXmlFn())) {

            List<UserDataSet> users = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                AddressDataSet address = new AddressDataSet("Street #" + i, 20 + i);
                PhoneDataSet phone = new PhoneDataSet(8452, String.format("%d%d%d-%d%d%d", i, i + 1, i, i + 1, i + 2, i));
                users.add(new UserDataSet(20 + i, "user #" + i, address, phone));
                service.save(users.get(i));
            }

            List<UserDataSet> dbusers = service.readAll();
            Assert.assertTrue(dbusers.equals(users));
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
