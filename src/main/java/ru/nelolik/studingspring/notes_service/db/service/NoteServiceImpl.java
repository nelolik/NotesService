package ru.nelolik.studingspring.notes_service.db.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.nelolik.studingspring.notes_service.config.CacheNames;
import ru.nelolik.studingspring.notes_service.db.dao.NotesDAO;
import ru.nelolik.studingspring.notes_service.db.dataset.Note;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NotesDAO notesDAO;

    @Override
    public List<Note> getAllNotes() {
        return notesDAO.getAllNotes();
    }

    @Override
    @Cacheable(value = CacheNames.NOTES, key = "#userId")
    public List<Note> getNotesByUserId(long userId) {
        return notesDAO.getNotesByUserId(userId);
    }

    @Override
    public Note getNoteById(long noteId) {
        return notesDAO.getOneNote(noteId);
    }

    @Override
    @CacheEvict(value = CacheNames.NOTES, key = "#note.getUserId()")
    public long addNote(Note note) {
        return notesDAO.addNote(note);
    }


    @Override
    @CacheEvict(value = CacheNames.NOTES, key = "#userId")
    public void removeNotesByUserId(long userId) {
        notesDAO.removeNotesByUserId(userId);
    }

    @Override
    @CacheEvict(value = CacheNames.NOTES, key = "#userId")
    public void removeNote(long id, long userId) {
        notesDAO.removeNoteByNoteId(id);
    }

}
