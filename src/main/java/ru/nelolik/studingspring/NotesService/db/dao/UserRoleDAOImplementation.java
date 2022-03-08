package ru.nelolik.studingspring.NotesService.db.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class UserRoleDAOImplementation implements UserRoleDAO{

    private SessionFactory sessionFactory;

    @Autowired
    public UserRoleDAOImplementation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserRole> getByUserid(Long userid) {
        List<UserRole> roles = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserRole> criteriaQuery = criteriaBuilder.createQuery(UserRole.class);
            Root<UserRole> root = criteriaQuery.from(UserRole.class);
            ParameterExpression<Long> parameter = criteriaBuilder.parameter(Long.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("userid"), parameter));
            Query<UserRole> query = session.createQuery(criteriaQuery);
            query.setParameter(parameter, userid);
            roles = query.getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public long saveUserRole(Long userid, String userRole) {
        UserRole userRoleObj = new UserRole(userid, userRole);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            long id = (Long) session.save(userRoleObj);
            transaction.commit();
            return id;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void deleteByUserid(Long userid) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaDelete<UserRole> criteria = criteriaBuilder.createCriteriaDelete(UserRole.class);
            Root<UserRole> root = criteria.from(UserRole.class);
            criteria.where(criteriaBuilder.equal(root.get("userid"), userid));
            session.createQuery(criteria).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
