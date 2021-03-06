package gui.buttons;

import application.GradingApplication;
import gui.pages.AddCourseFromHistoryPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCourseFromHistoryButton extends JButton implements ActionListener {

    public AddCourseFromHistoryButton() {
        super("Add courses from historical data");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GradingApplication.PAGE_LOADER.loadNewPage(new AddCourseFromHistoryPage());
    }
}
