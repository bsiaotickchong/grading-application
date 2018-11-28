package gui;

import org.jooq.grading_app.db.h2.tables.pojos.Course;

import javax.swing.*;
import java.awt.*;

public class CourseBox extends JPanel {
    public static final int PADDING = 10;
    public CourseBox(Course course) {
        super(new SpringLayout());
        setPreferredSize(new Dimension(200, 50));

        JLabel nameLabel = new JLabel(course.getName());
        JLabel descLabel = new JLabel(course.getDescription());
        add(nameLabel);
        add(descLabel);

        SpringLayout layout = (SpringLayout) this.getLayout();

        // constraints on nameLabel
        layout.putConstraint(SpringLayout.WEST, nameLabel,
                PADDING,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, nameLabel,
                PADDING,
                SpringLayout.NORTH, this);

        // constraints on descLabel
        layout.putConstraint(SpringLayout.EAST, descLabel,
                -PADDING,
                SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, descLabel,
                PADDING,
                SpringLayout.NORTH, this);
    }
}
