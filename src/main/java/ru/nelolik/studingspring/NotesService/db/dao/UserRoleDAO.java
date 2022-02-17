package ru.nelolik.studingspring.NotesService.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nelolik.studingspring.NotesService.db.dataset.UserRole;

import java.util.List;

public interface UserRoleDAO {
    List<UserRole> getByUserid(Long userid);
    long saveUserRole(Long userid, String userRole);
    void deleteByUserid(Long userid);
}