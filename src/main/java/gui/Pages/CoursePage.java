package gui.Pages;

import courses.CourseMetaData;
import gui.BackButton;
import gui.CategoriesAndAssignmentsPanel;
import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.TimeOfYear;

import javax.swing.*;
import java.awt.*;

public class CoursePage extends Page {

    private final CourseMetaData courseMetaData;
    private CategoriesAndAssignmentsPanel categoriesAndAssignmentsPanel;

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

        TimeOfYear timeOfYear = courseMetaData.getTimeOfYear();
        JLabel timeOfYearLabel = new JLabel(timeOfYear.getSemester().getLiteral() + " " + timeOfYear.getYear().toString());

        BackButton backButton = new BackButton();

        try {
            int categoriesAndAssignmentsPanelWidth = screenSize.width/4;
            int categoriesAndAssignmentsPanelHeight = screenSize.height/2;
            categoriesAndAssignmentsPanel = new CategoriesAndAssignmentsPanel(courseMetaData,
                    this,
                    categoriesAndAssignmentsPanelWidth,
                    categoriesAndAssignmentsPanelHeight);
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

        GridBagConstraints timeOfYearGBC = new GridBagConstraints();
        timeOfYearGBC.gridwidth = GridBagConstraints.REMAINDER;
        timeOfYearGBC.fill = GridBagConstraints.HORIZONTAL;
        timeOfYearGBC.anchor = GridBagConstraints.EAST;
        timeOfYearGBC.gridx = 0;
        timeOfYearGBC.gridy = 2;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 3;

        GridBagConstraints categoriesAndAssignmentsGBC = new GridBagConstraints();
        categoriesAndAssignmentsGBC.gridx = 0;
        categoriesAndAssignmentsGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(timeOfYearLabel, timeOfYearGBC);
        add(backButton, backButtonGBC);
        add(categoriesAndAssignmentsPanel, categoriesAndAssignmentsGBC);

        invalidate();
        repaint();
    }

    public CategoriesAndAssignmentsPanel getCategoriesAndAssignmentsPanel() {
        return this.categoriesAndAssignmentsPanel;
    }

    @Override
    public void redrawPage() {
        this.categoriesAndAssignmentsPanel.redrawPanel();
        super.redrawPage();
    }
}
