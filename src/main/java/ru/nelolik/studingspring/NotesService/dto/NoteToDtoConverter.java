package ru.nelolik.studingspring.NotesService.dto;

import org.mapstruct.Mapper;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteToDtoConverter {

    NoteDTO noteToDto(Note note);

    List<NoteDTO> notesToDto(List<Note> notes);

    Note noteDtoToNote(NoteDTO dto);

    List<Note> noteDtosToNotes(List<NoteDTO> dtos);
}
