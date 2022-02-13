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
    Long user_id;

    @Column(name = "roles")
    String roles;

//    @ManyToOne
//    @JoinTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @Transient
    Set<User> users;
}
