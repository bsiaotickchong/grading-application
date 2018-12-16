package gui.pages;

import courses.CourseMetaData;
import gui.buttons.BackButton;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;

public class CalculateStudentFinalGradePage extends Page {

    private final CourseMetaData courseMetaData;
    private final StudentMetaData studentMetaData;

    public CalculateStudentFinalGradePage(CourseMetaData courseMetaData,
                                          StudentMetaData studentMetaData) {
        super("Current Final Grade Calculation", "This page shows the calculation of the student's current final grade based on assignments and the student's grades.");

        this.courseMetaData = courseMetaData;
        this.studentMetaData = studentMetaData;
    }
    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        JLabel currentFinalGrade = new JLabel(String.format("Current final grade: %.2f%%", courseMetaData.getCurrentGrade(studentMetaData)));
        currentFinalGrade.setFont(new Font(currentFinalGrade.getFont().getName(), Font.PLAIN, 40));

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

        GridBagConstraints currentFinalGradeGBC = new GridBagConstraints();
        currentFinalGradeGBC.gridx = 0;
        currentFinalGradeGBC.gridy = 2;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(currentFinalGrade, currentFinalGradeGBC);
        add(backButton, backButtonGBC);
    }
}
