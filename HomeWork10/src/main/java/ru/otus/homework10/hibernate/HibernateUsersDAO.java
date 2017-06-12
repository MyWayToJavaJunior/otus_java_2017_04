package ru.otus.homework10.hibernate;

import org.hibernate.Session;
import ru.otus.homework10.common.db.datasets.UserDataSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

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

    public List<UserDataSet> getAllUsers() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        criteria.from(UserDataSet.class);
        return session.createQuery(criteria).list();
    }
}
