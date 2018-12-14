package gui;

import application.GradingApplication;
import courses.CourseMetaData;
import courses.CourseMetaDataManager;
import database.MetaData;
import gui.Pages.CoursePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class CourseList extends JScrollPane implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(CourseList.class);

    public CourseList(JPanel courseListPanel) {
        super(courseListPanel);

        setBorder(BorderFactory.createLineBorder(Color.black));
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));

        try {
            populateListWithCourses(courseListPanel);
        } catch (Exception e) {
            LOG.error("Couldn't create list of courses {}", e.getMessage());
        }
    }

    private void populateListWithCourses(JPanel courseListPanel) throws SQLException {
        CourseMetaDataManager courseMetaDataManager = new CourseMetaDataManager();

        List<MetaData> courseMetaDatas = courseMetaDataManager.getAllMetaData();

        for (MetaData courseMetaData : courseMetaDatas) {
            CourseBox courseBox = new CourseBox((CourseMetaData) courseMetaData, 200, 50);
            courseListPanel.add(courseBox);
            courseBox.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CourseBox courseBox = (CourseBox) e.getSource();

        GradingApplication.PAGE_LOADER.loadNewPage(new CoursePage(courseBox.getCourseMetaData()));
    }
}
