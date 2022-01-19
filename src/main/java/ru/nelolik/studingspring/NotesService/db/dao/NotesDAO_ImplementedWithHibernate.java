package ru.nelolik.studingspring.NotesService.db.dao;

import lombok.NoArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
@Repository
@NoArgsConstructor
public class NotesDAO_ImplementedWithHibernate implements NotesDAO{

    private SessionFactory sessionFactory;

    @Autowired
    public NotesDAO_ImplementedWithHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Note getOneNote(long noteId) {
        Note note = null;
        try {
            Session session = sessionFactory.openSession();
            note = session.get(Note.class,noteId);
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return note;
    }

    @Override
    public List<Note> getNotesByUserId(long userId) {
        List<Note> list = null;
        try {
            Session session = sessionFactory.openSession();
            CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
            var criteria = criteriaBuilder.createQuery(Note.class);
            Root<Note> root = criteria.from(Note.class);
            criteria.where(criteriaBuilder.equal(root.get("userId"), userId));
            list = session.createQuery(criteria).list();
            session.close();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Note> getAllNotes() {
        List<Note> list = null;
        try {
            Session session = sessionFactory.openSession();
            String sql = "from " + Note.class.getSimpleName();
            list = session.createQuery(sql).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public long addNote(Note note) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Long id = (Long) session.save(note);
            transaction.commit();
            session.close();
            return id;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void removeNoteByNoteId(long noteId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaDelete<Note> criteriaDelete = criteriaBuilder.createCriteriaDelete(Note.class);
            Root<Note> root = criteriaDelete.from(Note.class);
            criteriaDelete.where(criteriaBuilder.equal(root.get("id"), noteId));
            session.createQuery(criteriaDelete).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeNotesByUserId(long userId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaDelete<Note> criteriaDelete = criteriaBuilder.createCriteriaDelete(Note.class);
            Root<Note> root = criteriaDelete.from(Note.class);
            criteriaDelete.where(criteriaBuilder.equal(root.get("userId"), userId));
            session.createQuery(criteriaDelete).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }
}
