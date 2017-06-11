package ru.otus.homework10.hibernate;

import org.hibernate.Session;
import ru.otus.homework10.common.UserDataSet;

public class HibernateUsersDAO {
    private final Session session;

    public HibernateUsersDAO(Session session) {
        this.session = session;
    }

    public UserDataSet getUser(Long id) {
        return session.load(UserDataSet.class, id);
    }

    public boolean updateUser(UserDataSet user) {
        return session.save(user) != null;
    }
}
