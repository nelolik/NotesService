package ru.nelolik.studingspring.NotesService.db.dataset;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_role")
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    @Id
    @Column(name = "user_id", nullable = false)
    Long userid;

    @Column(name = "roles")
    String roles;

}
