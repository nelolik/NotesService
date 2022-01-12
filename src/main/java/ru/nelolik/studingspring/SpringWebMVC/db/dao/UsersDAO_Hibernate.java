package ru.nelolik.studingspring.SpringWebMVC.db.dao;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;
import ru.nelolik.studingspring.SpringWebMVC.db.dataset.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class UsersDAO_Hibernate implements UsersDao{

    private Session session;
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public UsersDAO_Hibernate(Session session, Configuration configuration, SessionFactory sessionFactory) {
        this.session = session;
        this.configuration = configuration;
        this.sessionFactory = sessionFactory;
    }

    public UsersDAO_Hibernate() {
    }

    @Override
    public List<User> index() {
        List<User> list = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "from " + User.class.getSimpleName();
            list = session.createQuery(sql).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public User user(long id) {
        User user = null;
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
            session.createQuery(criteriaQuery).executeUpdate();
//            user = session.get(User.class, id);
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public long insert(User user) {

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
    public void edit(User user) {

    }

    @Override
    public void delete(long id) {
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
