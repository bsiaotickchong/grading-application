package gui.students;

import gui.editables.EditableNoteText;
import gui.editables.EditableTextField;
import gui.pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.Note;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;

public class StudentNoteBox extends JButton {

    private final StudentMetaData studentMetaData;
    private Note note;

    public StudentNoteBox(StudentMetaData studentMetaData,
                          Note note,
                          Page parentPage,
                          int width,
                          int height) {
        super();
        this.studentMetaData = studentMetaData;
        this.note = note;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        this.setEnabled(false);

        EditableTextField noteText = new EditableNoteText(note, studentMetaData, parentPage);

        // TODO: add dates to note table so they can be displayed here

        GridBagConstraints noteTextGBC = new GridBagConstraints();
        noteTextGBC.anchor = GridBagConstraints.WEST;
        noteTextGBC.fill = GridBagConstraints.HORIZONTAL;
        noteTextGBC.weightx = 1.0;
        noteTextGBC.gridx = 0;

        add(noteText, noteTextGBC);
    }
}
