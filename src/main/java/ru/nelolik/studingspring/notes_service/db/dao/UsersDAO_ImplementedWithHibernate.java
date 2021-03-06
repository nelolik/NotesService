package ru.nelolik.studingspring.notes_service.db.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.nelolik.studingspring.notes_service.db.dataset.User;
import ru.nelolik.studingspring.notes_service.db.dataset.UserRole;
import ru.nelolik.studingspring.notes_service.db.exception.DataBaseException;

import javax.persistence.criteria.*;
import java.util.List;

@Slf4j
@Repository
public class UsersDAO_ImplementedWithHibernate implements UsersDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Autowired
    public UsersDAO_ImplementedWithHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list;
        try (Session session = sessionFactory.openSession()) {
            String sql = "from " + User.class.getSimpleName();
            list = session.createQuery(sql).list();
            for (User u :
                    list) {
                u.setRoles(userRoleDAO.getByUserid(u.getId()));
            }
        } catch (HibernateException e) {
            log.error("Error while reading all users from users db");
            throw new DataBaseException(e);
        }

        return list;
    }

    @Override
    public User getUserById(long id) {
        User user;
        try {
            Session session = sessionFactory.openSession();
            user = session.get(User.class, id);
            if (user == null) {
                return null;
            }
            user.setRoles(userRoleDAO.getByUserid(user.getId()));
            session.close();
        } catch (HibernateException e) {
            log.error("Error while reading user by user Id from users db");
            throw new DataBaseException(e);
        }

        return user;
    }

    @Override
    public User getUserByName(String name) {
        User user;
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
            ParameterExpression<String> parameter = criteriaBuilder.parameter(String.class);
            Root<User> root = criteria.from(User.class);
            criteria.select(root).where(criteriaBuilder.equal(root.get("username"), parameter));
            Query<User> query = session.createQuery(criteria);
            query.setParameter(parameter, name);
            user = query.uniqueResult();
            session.close();
        } catch (HibernateException e) {
            log.error("Error while reading user by userName from users db");
            throw new DataBaseException(e);
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
            transaction.begin();
            List<UserRole> roles = user.getRoles();
            for (UserRole r :
                    roles) {
                userRoleDAO.saveUserRole(id, r.getRoles());
            }
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            log.error("Error while inserting user to users db");
            throw new DataBaseException(e);
        }
    }

    @Override
    public void editUser(User user) {
        Transaction transaction;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (HibernateException e) {
            log.error("Error while editing user from users db");
            throw new DataBaseException(e);
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
            log.error("Error while deleting user by userId from users db");
            throw new DataBaseException(e);
        }
    }

}
