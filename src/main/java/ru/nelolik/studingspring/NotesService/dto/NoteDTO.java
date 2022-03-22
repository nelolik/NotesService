package ru.nelolik.studingspring.NotesService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {

    Long id;

    Long userId;

    String record;
}
