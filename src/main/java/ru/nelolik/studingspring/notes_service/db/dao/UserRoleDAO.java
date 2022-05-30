package ru.nelolik.studingspring.notes_service.db.dao;

import ru.nelolik.studingspring.notes_service.db.dataset.UserRole;

import java.util.List;

public interface UserRoleDAO {
    List<UserRole> getByUserid(Long userid);
    long saveUserRole(Long userid, String userRole);
    void deleteByUserid(Long userid);
}