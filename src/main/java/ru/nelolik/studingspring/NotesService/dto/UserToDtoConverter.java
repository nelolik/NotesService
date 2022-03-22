package ru.nelolik.studingspring.NotesService.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;
import ru.nelolik.studingspring.NotesService.db.dataset.Role;
import ru.nelolik.studingspring.NotesService.db.dataset.User;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

import java.util.Collections;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserToDtoConverter {
    String DEFAULT_ROLES =
            "Collections.singletonList(new UserRole(0L, Role.ROLE_USER.name()))";

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username")
    })
    UserDTO userToDto(User user);

    List<UserDTO> usersToDto(List<User> users);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(target = "password", defaultValue = ""),
            @Mapping(target = "roles", expression = DEFAULT_ROLES)
    })
    User userDtoToUser(UserDTO dto);

    List<User> userDtosToUsers(List<UserDTO> dtos);
}
