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
import java.sql.SQLException;

public class AddCategoryPage extends Page implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddCategoryPage.class);

    private final CourseMetaData courseMetaData;
    private JTextField categoryNameTextField;

    public AddCategoryPage(CourseMetaData courseMetaData) {
        super("Add Category", "Create a category for assignments");

        this.courseMetaData = courseMetaData;
    }

    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        categoryNameTextField = new JTextField("Enter category name here...");

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

        GridBagConstraints categoryNameGBC = new GridBagConstraints();
        categoryNameGBC.gridx = 0;
        categoryNameGBC.gridy = 2;

        GridBagConstraints submitButtonGBC = new GridBagConstraints();
        submitButtonGBC.gridx = 0;
        submitButtonGBC.gridy = 3;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(categoryNameTextField, categoryNameGBC);
        add(submitButton, submitButtonGBC);
        add(backButton, backButtonGBC);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            courseMetaData.addCategory(categoryNameTextField.getText());
        } catch (SQLException s) {
            LOG.error("Could not add category: {}", s.getMessage());
        }

        GradingApplication.PAGE_LOADER.loadPreviousPage();
    }
}
