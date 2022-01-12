package ru.nelolik.studingspring.SpringWebMVC.db.dataset;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "notes")
@Getter
@Setter
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "record")
    private String record;

    public Note() {
    }
}
