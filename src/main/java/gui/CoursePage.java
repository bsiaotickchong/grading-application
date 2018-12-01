package gui;

import courses.CourseMetaData;

import javax.swing.*;
import java.awt.*;

public class CoursePage extends Page {

    private final CourseMetaData courseMetaData;

    public CoursePage(CourseMetaData courseMetaData) {
        super(courseMetaData.getName(), courseMetaData.getDescription());
        this.courseMetaData = courseMetaData;
    }

    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        BackButton backButton = new BackButton();

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
