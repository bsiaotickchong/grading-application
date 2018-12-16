package gui.buttons;

import application.GradingApplication;
import courses.CourseMetaData;
import gui.pages.StudentPage;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentCourseButton extends JButton implements ActionListener {

    private final StudentMetaData studentMetaData;
    private final CourseMetaData courseMetaData;

    public StudentCourseButton(StudentMetaData studentMetaData,
                               CourseMetaData courseMetaData,
                               int width,
                               int height) {
        super(courseMetaData.getName());
        this.studentMetaData = studentMetaData;
        this.courseMetaData = courseMetaData;
        addActionListener(this);
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GradingApplication.PAGE_LOADER.loadNewPage(new StudentPage(studentMetaData, courseMetaData));
    }
}
