package ru.nelolik.studingspring.NotesService.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}