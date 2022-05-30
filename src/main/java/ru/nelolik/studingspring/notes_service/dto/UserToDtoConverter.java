package ru.nelolik.studingspring.notes_service.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.nelolik.studingspring.notes_service.db.dataset.Role;
import ru.nelolik.studingspring.notes_service.db.dataset.User;
import ru.nelolik.studingspring.notes_service.db.dataset.UserRole;

import java.util.Collections;
import java.util.List;

@Component
@Mapper(componentModel = "spring",
        imports = {Role.class, UserRole.class, Collections.class})
public interface UserToDtoConverter {
    String DEFAULT_ROLES =
            "java(Collections.singletonList(new UserRole(0L, Role.ROLE_USER.name())))";

    UserDTO userToDto(User user);

    List<UserDTO> usersToDto(List<User> users);

    @Mapping(target = "password", constant = "")
    @Mapping(target = "roles", expression = DEFAULT_ROLES)
    User userDtoToUser(UserDTO dto);

    List<User> userDtosToUsers(List<UserDTO> dtos);
}
