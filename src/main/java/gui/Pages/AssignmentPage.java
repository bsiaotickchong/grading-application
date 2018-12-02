package gui.Pages;

import assignments.AssignmentMetaData;
import gui.BackButton;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;

import javax.swing.*;
import java.awt.*;

public class AssignmentPage extends Page {

    private AssignmentMetaData assignmentMetaData;

    public AssignmentPage(AssignmentMetaData assignmentMetaData, StudentType studentType) {
        super("Assignment: " + assignmentMetaData.getName(), "View grades for this assignment");
        this.assignmentMetaData = assignmentMetaData;
    }

    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

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

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 2;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(backButton, backButtonGBC);
    }
}
