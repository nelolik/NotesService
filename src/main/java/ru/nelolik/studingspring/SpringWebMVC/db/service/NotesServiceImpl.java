package ru.nelolik.studingspring.SpringWebMVC.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.nelolik.studingspring.SpringWebMVC.db.dao.NotesDAO;
import ru.nelolik.studingspring.SpringWebMVC.db.dataset.Note;

import java.util.List;

public class NotesServiceImpl implements NotesService{

    private NotesDAO notesDAO;

    @Autowired
    public NotesServiceImpl(NotesDAO notesDAO) {
        this.notesDAO = notesDAO;
    }

    @Override
    public List<Note> index() {
        return notesDAO.getAllNotes();
    }

    @Override
    public List<Note> getNotesByUserId(long userId) {
        return notesDAO.getNotesByUserId(userId);
    }

    @Override
    public Note getNoteById(long noteId) {
        return notesDAO.getOneNote(noteId);
    }

    @Override
    public long addNote(Note note) {
        return notesDAO.addNote(note);
    }


    @Override
    public void removeUserNotes(long userId) {
        notesDAO.removeUserNotes(userId);
    }

    @Override
    public void removeNote(long id) {
        notesDAO.removeNote(id);
    }
}