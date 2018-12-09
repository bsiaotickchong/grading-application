package gui;

import application.GradingApplication;
import courses.CourseMetaData;
import gui.Pages.AddCategoryPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCategoryButton extends JButton implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddCategoryButton.class);

    private final CourseMetaData courseMetaData;

    public AddCategoryButton(CourseMetaData courseMetaData) {
        super("Add Category");
        addActionListener(this);
        this.courseMetaData = courseMetaData;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.debug("Clicked AddCategoryButton");

        GradingApplication.PAGE_LOADER.loadNewPage(new AddCategoryPage(courseMetaData));
    }
}
