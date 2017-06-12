package ru.otus.homework10.hibernate;

import org.hibernate.ReplicationMode;
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
        return session.get(UserDataSet.class, id);
    }

    public boolean updateUser(UserDataSet user) {
        if (user.getId() == null || user.getId() <= 0) {
            user.setId(null);
            session.save(user);
        }
        else {
            session.replicate(user, ReplicationMode.OVERWRITE);
        }

        return true;
    }

    public List<UserDataSet> getAllUsers() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        criteria.from(UserDataSet.class);
        return session.createQuery(criteria).list();
    }
}
