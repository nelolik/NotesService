package ru.nelolik.studingspring.NotesService.dto;

import lombok.*;
import ru.nelolik.studingspring.NotesService.db.dataset.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
