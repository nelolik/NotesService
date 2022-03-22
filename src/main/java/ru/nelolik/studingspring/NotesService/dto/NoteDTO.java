package ru.nelolik.studingspring.NotesService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;

@Getter
@Setter
@AllArgsConstructor
public class NoteDTO {

    private Long id;

    private Long userId;

    private String record;

    public NoteDTO(Note note) {
        this.id = note.getId();
        this.userId = note.getUserId();
        this.record = note.getRecord();
    }
}
