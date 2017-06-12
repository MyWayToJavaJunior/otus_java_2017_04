package ru.otus.homework10.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.homework10.common.db.datasets.AddressDataSet;
import ru.otus.homework10.common.db.DBSettings;
import ru.otus.homework10.common.db.datasets.PhoneDataSet;
import ru.otus.homework10.common.db.datasets.UserDataSet;
import ru.otus.homework10.common.db.ConnectionHelper;
import ru.otus.homework10.common.db.IDatabaseService;

import java.sql.SQLException;
import java.util.List;

public class HibernateDatabaseService implements IDatabaseService {
    private final DBSettings settings;
    private SessionFactory sessionFactory;
    private Session session;
    private HibernateUsersDAO dao;

    public HibernateDatabaseService() {
        settings = DBSettings.getInstance();
    }

    @Override
    public void save(UserDataSet dataSet) throws SQLException {
        Transaction transaction = session.beginTransaction();
        dao.updateUser(dataSet);
        transaction.commit();
    }

    @Override
    public UserDataSet read(long id) throws SQLException {
        return dao.getUser(id);
    }

    @Override
    public List<UserDataSet> readAll() {
        return dao.getAllUsers();
    }

    @Override
    public void loadConfiguration(String configuartionFileName) {

        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

        configuration.setProperty("hibernate.connection.url", ConnectionHelper.buildShortURL(settings));
        configuration.setProperty("hibernate.connection.username", settings.getLogin());
        configuration.setProperty("hibernate.connection.password", settings.getPassword());
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        closeSessionFactory();
        sessionFactory = createSessionFactory(configuration);

    }

    @Override
    public void openConnection() {
        closeConnection();
        session = sessionFactory.openSession();
        dao = new HibernateUsersDAO(session);
    }

    @Override
    public void closeConnection() {
        if (session != null) {
            session.close();
        }
        session = null;
        dao = null;
    }

    private void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Override
    public void close() {
        closeConnection();
        closeSessionFactory();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
