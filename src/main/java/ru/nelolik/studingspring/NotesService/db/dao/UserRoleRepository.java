package ru.nelolik.studingspring.NotesService.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

import java.util.List;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserid(Long userid);
}