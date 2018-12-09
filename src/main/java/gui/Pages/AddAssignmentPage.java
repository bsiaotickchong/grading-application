package gui.Pages;

import application.GradingApplication;
import courses.CourseMetaData;
import gui.BackButton;
import org.jooq.grading_app.db.h2.tables.pojos.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddAssignmentPage extends Page implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddAssignmentPage.class);

    private final CourseMetaData courseMetaData;
    private final Category category;
    private JTextField assignmentNameTextField;

    public AddAssignmentPage(CourseMetaData courseMetaData,
                             Category category) {
        super("Add Assignment", "Create an assignment for category: " + category.getName());
        this.courseMetaData = courseMetaData;
        this.category = category;
    }

    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        assignmentNameTextField = new JTextField("Enter assignment name here...");

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        JButton backButton = new BackButton();

        GridBagConstraints titleGBC = new GridBagConstraints();
        titleGBC.anchor = GridBagConstraints.WEST;
        titleGBC.gridx = 0;
        titleGBC.gridy = 0;

        GridBagConstraints descriptionGBC = new GridBagConstraints();
        descriptionGBC.gridwidth = GridBagConstraints.REMAINDER;
        descriptionGBC.fill = GridBagConstraints.HORIZONTAL;
        descriptionGBC.gridx = 0;
        descriptionGBC.gridy = 1;

        GridBagConstraints assignmentNameGBC = new GridBagConstraints();
        assignmentNameGBC.gridx = 0;
        assignmentNameGBC.gridy = 2;

        GridBagConstraints submitButtonGBC = new GridBagConstraints();
        submitButtonGBC.gridx = 0;
        submitButtonGBC.gridy = 3;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(assignmentNameTextField, assignmentNameGBC);
        add(submitButton, submitButtonGBC);
        add(backButton, backButtonGBC);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        courseMetaData.addAssignment(category, false, assignmentNameTextField.getText());

        GradingApplication.PAGE_LOADER.loadPreviousPage();
    }
}
