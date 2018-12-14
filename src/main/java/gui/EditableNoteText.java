package gui;

import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.Note;
import students.StudentMetaData;

import java.sql.SQLException;

public class EditableNoteText extends EditableTextField {

    private Note note;
    private StudentMetaData studentMetaData;

    public EditableNoteText(Note note,
                            StudentMetaData studentMetaData,
                            Page parentPage) {
        super(note.getNoteText(), parentPage);
        this.note = note;
        this.studentMetaData = studentMetaData;
    }

    @Override
    void updateText(String updatedText) throws SQLException {
        studentMetaData.updateNote(note, updatedText);
        note.setNoteText(updatedText);
        getParentPage().redrawPage();
    }
}
