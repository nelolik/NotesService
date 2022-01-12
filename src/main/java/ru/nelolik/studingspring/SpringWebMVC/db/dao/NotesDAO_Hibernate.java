package ru.nelolik.studingspring.SpringWebMVC.db.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;
import ru.nelolik.studingspring.SpringWebMVC.db.dataset.Note;

import java.util.List;

@Component
public class NotesDAO_Hibernate implements NotesDAO{

    private Session session;
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public NotesDAO_Hibernate(Session session, Configuration configuration, SessionFactory sessionFactory) {
        this.session = session;
        this.configuration = configuration;
        this.sessionFactory = sessionFactory;
    }

    public NotesDAO_Hibernate() {
    }

    @Override
    public List<String> getOneNote(long noteId) {
        return null;
    }

    @Override
    public List<String> getAllNotes(long userId) {
        return null;
    }

    @Override
    public long addNote(Note note) {
        return 0;
    }

    @Override
    public long removeNote(long noteId) {
        return 0;
    }

    @Override
    public void removeUserNotes(long userId) {

    }
}
