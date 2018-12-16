package gui.buttons;

import application.GradingApplication;
import gui.pages.AddCoursePage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCourseButton extends JButton implements ActionListener {

    public AddCourseButton() {
        super("Add Course");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GradingApplication.PAGE_LOADER.loadNewPage(new AddCoursePage());
    }
}
