package ru.nelolik.studingspring.NotesService.db.dataset;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@Setter
@Getter
public class Role {
    @Id
    @Column (name = "role_id", nullable = false, unique = true)
    Long role_id;

    @Column(name = "role_name", nullable = false, unique = true)
    String role_name;

//    ROLE_USER,
//    ROLE_ADMIN
}
