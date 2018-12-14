package gui;

import courses.CourseMetaData;
import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class StudentNoteList extends JScrollPane {

    private final static Logger LOG = LoggerFactory.getLogger(StudentNoteList.class);

    private final StudentMetaData studentMetaData;
    private final CourseMetaData courseMetaData;
    private final Page parentPage;
    private final int width;

    public StudentNoteList(StudentMetaData studentMetaData,
                           CourseMetaData courseMetaData,
                           Page parentPage,
                           int width) {
        super(new JPanel());
        this.studentMetaData = studentMetaData;
        this.courseMetaData = courseMetaData;
        this.parentPage = parentPage;
        this.width = width;

        JPanel studentNoteListPanel = (JPanel) this.getViewport().getView();
        studentNoteListPanel.setLayout(new BoxLayout(studentNoteListPanel, BoxLayout.Y_AXIS));

        populateList(studentNoteListPanel);
    }

    private void populateList(JPanel studentNoteListPanel) {
        try {
            populateListWithNotes(studentNoteListPanel);
        } catch (Exception e) {
            LOG.error("Couldn't create StudentNoteList {}", e.getMessage());
        }

        addAddNoteButton(studentNoteListPanel);
    }

    private void populateListWithNotes(JPanel studentNoteListPanel) throws SQLException {
        List<Note> notes = studentMetaData.getNotes(courseMetaData.getCourse());

        for (Note note : notes) {
            JButton studentNoteBox = new StudentNoteBox(studentMetaData, note, parentPage, width-20, 50);
            studentNoteListPanel.add(studentNoteBox);
        }
    }

    private void addAddNoteButton(JPanel studentNoteListPanel) {
        JButton addNoteButton = new AddNoteButton(studentMetaData, courseMetaData, parentPage);
        studentNoteListPanel.add(addNoteButton);
        addNoteButton.grabFocus(); // TODO: temp fix for focus always being grabbed by first name
    }

    public void updateNoteList() {
        JPanel studentNoteListPanel = (JPanel) this.getViewport().getView();
        studentNoteListPanel.removeAll();

        populateList(studentNoteListPanel);

        revalidate();
        repaint();
    }
}
