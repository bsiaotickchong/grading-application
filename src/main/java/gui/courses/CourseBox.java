package gui.courses;

import courses.CourseMetaData;
import org.jooq.grading_app.db.h2.tables.pojos.Course;
import org.jooq.grading_app.db.h2.tables.pojos.TimeOfYear;

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
        JPanel nameAndDescPanel = new JPanel();
        nameAndDescPanel.setLayout(new BoxLayout(nameAndDescPanel, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel(course.getName());
        JLabel descLabel = new JLabel(course.getDescription());
        nameAndDescPanel.setOpaque(false);

        nameAndDescPanel.add(nameLabel);
        nameAndDescPanel.add(new JLabel(" | "));
        nameAndDescPanel.add(descLabel);

        // time of year panel
        TimeOfYear timeOfYear = courseMetaData.getTimeOfYear();
        JLabel timeOfYearLabel = new JLabel(
                timeOfYear.getSemester().getLiteral() +
                        " " +
                        timeOfYear.getYear().toString()
        );

        // enrollment count is just a JLabel added to the buttonLayout
        JPanel enrollmentPanel = new JPanel();
        JLabel enrollmentLabel = new JLabel(Integer.toString(courseMetaData.getEnrollmentCount()));
        enrollmentPanel.add(enrollmentLabel);
        enrollmentPanel.setOpaque(false);
        enrollmentPanel.setToolTipText("Number of students enrolled in this courses");

        // create GridBagConstraints for the different panels
        GridBagConstraints nameAndDescGBC = new GridBagConstraints();
        nameAndDescGBC.anchor = GridBagConstraints.LINE_START;
        nameAndDescGBC.weightx = 1.0;
        nameAndDescGBC.gridx = 0;
        nameAndDescGBC.gridy = 0;
        nameAndDescGBC.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints timeOfYearGBC = new GridBagConstraints();
        timeOfYearGBC.anchor = GridBagConstraints.EAST;
        timeOfYearGBC.weightx = .1;
        timeOfYearGBC.gridx = 1;
        timeOfYearGBC.gridy = 0;

        GridBagConstraints enrollmentGBC = new GridBagConstraints();
        enrollmentGBC.anchor = GridBagConstraints.LINE_END;
        enrollmentGBC.weightx = .1;
        enrollmentGBC.gridx = 2;
        enrollmentGBC.gridy = 0;

        // add panels to button's GridBagLayout
        add(nameAndDescPanel, nameAndDescGBC);
        add(timeOfYearLabel, timeOfYearGBC);
        add(enrollmentPanel, enrollmentGBC);
    }

    public CourseMetaData getCourseMetaData() {
        return courseMetaData;
    }
}
