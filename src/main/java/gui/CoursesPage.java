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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        CourseList courseList = new CourseList(new JPanel());
//        courseList.setPreferredSize(new Dimension(200, 200));
        courseList.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/2));
        add(courseList, c);

    }
}
