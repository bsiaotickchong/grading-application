package gui.Pages;

import courses.CourseMetaData;
import gui.BackButton;
import gui.CategoriesAndAssignmentsPanel;
import gui.Pages.Page;

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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        BackButton backButton = new BackButton();

        CategoriesAndAssignmentsPanel categoriesAndAssignmentsPanel;
        try {
            int categoriesAndAssignmentsPanelWidth = screenSize.width/4;
            int categoriesAndAssignmentsPanelHeight = screenSize.height/2;
            categoriesAndAssignmentsPanel = new CategoriesAndAssignmentsPanel(courseMetaData, categoriesAndAssignmentsPanelWidth, categoriesAndAssignmentsPanelHeight);
            categoriesAndAssignmentsPanel.setPreferredSize(new Dimension(categoriesAndAssignmentsPanelWidth, categoriesAndAssignmentsPanelHeight));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

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

        GridBagConstraints categoriesAndAssignmentsGBC = new GridBagConstraints();
        categoriesAndAssignmentsGBC.gridx = 0;
        categoriesAndAssignmentsGBC.gridy = 3;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(backButton, backButtonGBC);
        add(categoriesAndAssignmentsPanel, categoriesAndAssignmentsGBC);

        invalidate();
        repaint();
    }
}
