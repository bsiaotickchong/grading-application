package gui.Pages;

import application.GradingApplication;
import courses.CourseMetaData;
import gui.BackButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

public class AddCoursePage extends Page implements ActionListener, ItemListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddCoursePage.class);

    private JTextField courseNameTextField;
    private JButton submitButton;

    public AddCoursePage() {
        super("Add Course", "Create a new course");
    }

    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        courseNameTextField = new JTextField("Enter course name here...");

        submitButton = new JButton("Submit");
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

        GridBagConstraints courseNameGBC = new GridBagConstraints();
        courseNameGBC.gridx = 0;
        courseNameGBC.gridy = 2;

        GridBagConstraints submitButtonGBC = new GridBagConstraints();
        submitButtonGBC.gridx = 0;
        submitButtonGBC.gridy = 3;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(courseNameTextField, courseNameGBC);
        add(submitButton, submitButtonGBC);
        add(backButton, backButtonGBC);
    }

    // for submit button
    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info("Submit button clicked");

        if (e.getSource() == submitButton) {
//            try {
//                CourseMetaData courseMetaData1 = new CourseMetaData(courseNameTextField.getText(), timeOfYears.get(0), "Class on object-oriented programming.");
//            } catch (SQLException s) {
//                LOG.error("Could not create course: {}", s.getMessage());
//            }

            GradingApplication.PAGE_LOADER.loadPreviousPage();
        }
    }

    // for time of year
    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
