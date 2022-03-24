package ru.nelolik.studingspring.NotesService.dto;

import lombok.*;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    Long id;

    String username;

}
