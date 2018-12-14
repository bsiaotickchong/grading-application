package gui.Pages;

import gui.BackButton;

import javax.swing.*;
import java.awt.*;

public class AddCourseFromHistoryPage extends Page {

    public AddCourseFromHistoryPage() {
        super("Add course from historical data", "Select a course to copy all the categories and assignments");
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
        backButtonGBC.anchor = GridBagConstraints.WEST;
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(backButton, backButtonGBC);
    }
}
