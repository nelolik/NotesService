package ru.nelolik.studingspring.NotesService.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nelolik.studingspring.NotesService.db.dao.NotesDAO;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;

import java.util.List;

@Component
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
