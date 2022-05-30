package ru.nelolik.studingspring.notes_service.db.dataset;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserRole {
    @Id
    @Column(name = "user_id", nullable = false)
    Long userid;

    @Column(name = "roles")
    String roles;

}
