package gui;

import courses.CourseMetaData;
import courses.CourseMetaDataManager;
import database.MetaData;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CoursesPage extends Page {

    public CoursesPage() {
        super("All Courses", "All courses are listed here.");
    }

    @Override
    public void loadPage() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        add(new CourseList(new JPanel()), c);
    }
}
