package gui.pages;

import gui.buttons.AddCourseButton;
import gui.buttons.AddCourseFromHistoryButton;
import gui.courses.CourseList;

import javax.swing.*;
import java.awt.*;

public class AllCoursesPage extends Page {

    public AllCoursesPage() {
        super("All Courses", "All courses are listed here.");
    }

    @Override
    public void loadPage() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        CourseList courseList = new CourseList(new JPanel());
        courseList.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/2));

        JButton addCourseButton = new AddCourseButton();
        JButton addCourseFromHistoryButton = new AddCourseFromHistoryButton();

        GridBagConstraints titleGBC = new GridBagConstraints();
        titleGBC.anchor = GridBagConstraints.WEST;
        titleGBC.gridx = 0;
        titleGBC.gridy = 0;

        GridBagConstraints courseListGBC = new GridBagConstraints();
        courseListGBC.gridwidth = GridBagConstraints.REMAINDER;
        courseListGBC.fill = GridBagConstraints.HORIZONTAL;
        courseListGBC.gridx = 0;
        courseListGBC.gridy = 1;

        GridBagConstraints addCourseButtonGBC = new GridBagConstraints();
        addCourseButtonGBC.anchor = GridBagConstraints.WEST;
        addCourseButtonGBC.gridx = 0;
        addCourseButtonGBC.gridy = 2;
        GridBagConstraints addCourseFromHistoryButtonGBC = new GridBagConstraints();
        addCourseFromHistoryButtonGBC.anchor = GridBagConstraints.WEST;
        addCourseFromHistoryButtonGBC.gridx = 0;
        addCourseFromHistoryButtonGBC.gridy = 3;

        add(title, titleGBC);
        add(courseList, courseListGBC);
        add(addCourseButton, addCourseButtonGBC);
        add(addCourseFromHistoryButton, addCourseFromHistoryButtonGBC);
    }
}
