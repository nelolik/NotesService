package ru.nelolik.studingspring.NotesService.db.dataset;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notes")
@Getter
@Setter
@AllArgsConstructor
public class Note {
    @Id
    @SequenceGenerator(name = "notes_id", sequenceName = "notes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notes_id" )
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "record")
    private String record;

    public Note() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id.equals(note.id) && userId.equals(note.userId) && record.equals(note.record);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, record);
    }
}
