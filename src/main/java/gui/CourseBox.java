package gui;

import courses.CourseMetaData;
import org.jooq.grading_app.db.h2.tables.pojos.Course;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CourseBox extends JButton {
    public static final int PADDING = 10;
    private CourseMetaData courseMetaData;

    public CourseBox(CourseMetaData courseMetaData, int width, int height) throws SQLException {
        super();
        this.courseMetaData = courseMetaData;

        Course course = courseMetaData.getCourse();

        GridBagLayout buttonLayout = new GridBagLayout();
        setLayout(buttonLayout);
        setPreferredSize(new Dimension(width, height));

        // Name and description are in a separate JPanel
        JPanel nameAndDescPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(course.getName());
        JLabel descLabel = new JLabel(course.getDescription());
        nameAndDescPanel.setOpaque(false);

        nameAndDescPanel.add(BorderLayout.WEST, nameLabel);
        nameAndDescPanel.add(BorderLayout.EAST, descLabel);

        // enrollment count is just a JLabel added to the buttonLayout
        JPanel enrollmentPanel = new JPanel();
        JLabel enrollmentLabel = new JLabel(Integer.toString(courseMetaData.getEnrollmentCount()));
        enrollmentPanel.add(enrollmentLabel);
        enrollmentPanel.setOpaque(false);

        // create GridBagConstraints for the different panels
        GridBagConstraints nameAndDescGBC = new GridBagConstraints();
        nameAndDescGBC.anchor = GridBagConstraints.LINE_START;
        nameAndDescGBC.weightx = 1.0;
        nameAndDescGBC.gridx = 0;
        nameAndDescGBC.gridy = 0;
        nameAndDescGBC.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints enrollmentGBC = new GridBagConstraints();
        enrollmentGBC.anchor = GridBagConstraints.LINE_END;
        enrollmentGBC.weightx = .1;
        enrollmentGBC.gridx = 1;
        enrollmentGBC.gridy = 0;
        enrollmentGBC.fill = GridBagConstraints.HORIZONTAL;

        // add panels to button's GridBagLayout
        add(nameAndDescPanel, nameAndDescGBC);
        add(enrollmentPanel, enrollmentGBC);
    }

    public CourseMetaData getCourseMetaData() {
        return courseMetaData;
    }
}
