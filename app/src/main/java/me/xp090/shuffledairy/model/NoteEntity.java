package me.xp090.shuffledairy.model;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;


@Entity
public class NoteEntity {

    @Id public long id;

    public long noteDate;

    public String noteContent;

    public int noteColor;

    public NoteEntity() {

    }
    public NoteEntity(long noteDate, String noteContent, int noteColor) {
        this.noteDate = noteDate;
        this.noteContent = noteContent;
        this.noteColor = noteColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NoteEntity) {
            NoteEntity noteEntityToCompare = (NoteEntity) obj;
           return noteEntityToCompare.id == this.id;
         }
        return false;
    }
}
