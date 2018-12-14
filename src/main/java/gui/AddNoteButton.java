package gui;

import application.GradingApplication;
import courses.CourseMetaData;
import gui.Pages.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddNoteButton extends JButton implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddNoteButton.class);

    private final StudentMetaData studentMetaData;
    private final CourseMetaData courseMetaData;
    private final Page parentPage;

    public AddNoteButton(StudentMetaData studentMetaData,
                         CourseMetaData courseMetaData,
                         Page parentPage) {
        super("Add note");
        addActionListener(this);
        this.studentMetaData = studentMetaData;
        this.courseMetaData = courseMetaData;
        this.parentPage = parentPage;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.debug("Clicked AddNoteButton");

        try {
            studentMetaData.addNote("", courseMetaData);
            LOG.debug("Note was added");
        } catch (SQLException s) {
            LOG.error("Note could not be added: {}", s.getMessage());
        }

        parentPage.redrawPage();
    }
}
