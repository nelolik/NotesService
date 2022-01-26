package ru.nelolik.studingspring.NotesService.db.dao;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
@Repository
public class UsersDAO_ImplementedWithHibernate implements UsersDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public UsersDAO_ImplementedWithHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public UsersDAO_ImplementedWithHibernate() {
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = null;
        try (Session session = sessionFactory.openSession()) {
            String sql = "from " + User.class.getSimpleName();
            list = session.createQuery(sql).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public User getUserById(long id) {
        User user = null;
        try {
            Session session = sessionFactory.openSession();
            user = session.get(User.class, id);
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getUserByName(String name) {
        User user = null;
        try {
            Session session = sessionFactory.openSession();
            user = session.get(User.class, name);
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public long insertUser(User user) {

        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Long id = (Long) session.save(user);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void editUser(User user) {
        Transaction transaction;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
            CriteriaDelete<User> criteria = criteriaBuilder.createCriteriaDelete(User.class);
            Root<User> root = criteria.from(User.class);
            criteria.where(criteriaBuilder.equal(root.get("id"), id));
            session.createQuery(criteria).executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

}
