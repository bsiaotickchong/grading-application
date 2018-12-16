package gui.buttons;

import application.GradingApplication;
import courses.CourseMetaData;
import gui.pages.CalculateStudentFinalGradePage;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculateStudentFinalGradeButton extends JButton implements ActionListener {

    private final CourseMetaData courseMetaData;
    private final StudentMetaData studentMetaData;

    public CalculateStudentFinalGradeButton(CourseMetaData courseMetaData,
                                            StudentMetaData studentMetaData) {
        super("Current final grade");
        addActionListener(this);
        this.courseMetaData = courseMetaData;
        this.studentMetaData = studentMetaData;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GradingApplication.PAGE_LOADER.loadNewPage(new CalculateStudentFinalGradePage(courseMetaData, studentMetaData));
    }
}
