package gui.buttons;

import application.GradingApplication;
import gui.pages.AddStudentPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import courses.CourseMetaData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddStudentButton extends JButton implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddStudentButton.class);

    private final CourseMetaData courseMetaData;

    public AddStudentButton(CourseMetaData courseMetaData) {
        super("Add Student");
        addActionListener(this);

        this.courseMetaData = courseMetaData;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.debug("Clicked AddStudentButton");

        GradingApplication.PAGE_LOADER.loadNewPage(new AddStudentPage(courseMetaData));
    }
}