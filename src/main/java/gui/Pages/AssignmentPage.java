package gui.Pages;

import assignments.AssignmentMetaData;
import courses.CourseMetaData;
import gui.AssignmentStudentList;
import gui.BackButton;
import gui.CategoriesAndAssignmentsPanel;
import gui.GradesAndStudentsPanel;
import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;

import javax.swing.*;
import java.awt.*;

public class AssignmentPage extends Page {

    StudentType studentType;
    AssignmentMetaData assignmentMetaData;
    CourseMetaData courseMetaData;
    private GradesAndStudentsPanel gradesAndStudentsPanel;

    public AssignmentPage(AssignmentMetaData A,
                          CourseMetaData courseMetaData,
                          StudentType studentType) {
        super("Category: " + A.getCategory().getName(), "Assignment: " + A.name);
        this.studentType = studentType;
        this.assignmentMetaData = A;
        this.courseMetaData = courseMetaData;
    }

    @Override
    public void loadPage(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());
        description.setFont(new Font(description.getFont().getName(), Font.PLAIN, titleFontSize));

        BackButton backButton = new BackButton();

        try {
            int AssignmentsPanelWidth = screenSize.width/3;
            int AssignmentsPanelHeight = screenSize.height/2;
            gradesAndStudentsPanel = new GradesAndStudentsPanel(assignmentMetaData,
                    this,
                    AssignmentsPanelWidth,
                    AssignmentsPanelHeight);
            gradesAndStudentsPanel.setPreferredSize(new Dimension(AssignmentsPanelWidth, AssignmentsPanelHeight));
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

        GridBagConstraints studentsOfAssignmentGBC = new GridBagConstraints();
        studentsOfAssignmentGBC.gridx = 0;
        studentsOfAssignmentGBC.gridy = 4;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.anchor = GridBagConstraints.WEST;
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 5;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(backButton, backButtonGBC);
        add(gradesAndStudentsPanel, studentsOfAssignmentGBC);
    }
}
