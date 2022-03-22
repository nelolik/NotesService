package ru.nelolik.studingspring.NotesService.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.nelolik.studingspring.NotesService.db.dataset.Note;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteToDtoConverter {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "record", target = "record")
    NoteDTO noteToDto(Note note);

    List<NoteDTO> notesToDto(List<Note> notes);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "record", target = "record")
    Note noteDtoToNote(NoteDTO dto);


    List<Note> noteDtosToNotes(List<NoteDTO> dtos);
}
