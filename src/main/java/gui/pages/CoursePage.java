package gui.pages;

import courses.CourseMetaData;
import gui.buttons.BackButton;
import gui.courses.CategoriesAndAssignmentsPanel;
import gui.students.StudentList;
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

        StudentList studentList;
        try {
            int categoriesAndAssignmentsPanelWidth = screenSize.width/4;
            int categoriesAndAssignmentsPanelHeight = screenSize.height/2;
            categoriesAndAssignmentsPanel = new CategoriesAndAssignmentsPanel(courseMetaData,
                    this,
                    categoriesAndAssignmentsPanelWidth,
                    categoriesAndAssignmentsPanelHeight);
            categoriesAndAssignmentsPanel.setPreferredSize(new Dimension(categoriesAndAssignmentsPanelWidth, categoriesAndAssignmentsPanelHeight));

            int studentListPanelWidth = screenSize.width/4;
            int studentListPanelHeight = screenSize.height/2;
            studentList = new StudentList(courseMetaData.getEnrolledStudentMetaDatas(), courseMetaData, this, studentListPanelWidth);
            studentList.setPreferredSize(new Dimension(studentListPanelWidth, studentListPanelHeight));
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

        GridBagConstraints categoriesAndAssignmentsGBC = new GridBagConstraints();
        categoriesAndAssignmentsGBC.gridx = 0;
        categoriesAndAssignmentsGBC.gridy = 4;

        GridBagConstraints studentListGBC = new GridBagConstraints();
        studentListGBC.gridx = 1;
        studentListGBC.gridy = 4;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.anchor = GridBagConstraints.WEST;
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 5;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(timeOfYearLabel, timeOfYearGBC);
        add(categoriesAndAssignmentsPanel, categoriesAndAssignmentsGBC);
        add(studentList, studentListGBC);
        add(backButton, backButtonGBC);

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
