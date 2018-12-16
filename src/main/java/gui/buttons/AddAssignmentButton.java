package gui.buttons;

import application.GradingApplication;
import courses.CourseMetaData;
import gui.pages.AddAssignmentPage;
import org.jooq.grading_app.db.h2.tables.pojos.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAssignmentButton extends JButton implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddAssignmentButton.class);
    private final CourseMetaData courseMetaData;
    private final Category category;

    public AddAssignmentButton(CourseMetaData courseMetaData,
                               Category category) {
        super("Add Assignment");
        addActionListener(this);

        this.courseMetaData = courseMetaData;
        this.category = category;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.debug("Clicked AddAssignmentButton");

        GradingApplication.PAGE_LOADER.loadNewPage(new AddAssignmentPage(courseMetaData, category));
    }
}
