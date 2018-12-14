package gui;

import assignments.AssignmentMetaData;
import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.AssignmentWeight;
import org.jooq.grading_app.db.h2.tables.pojos.StudentGrade;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentAssignmentBox extends JButton {

    private final AssignmentMetaData assignmentMetaData;
    private final StudentMetaData studentMetaData;

    public StudentAssignmentBox(StudentMetaData studentMetaData,
                                AssignmentMetaData assignmentMetaData,
                                Page parentPage,
                                int width,
                                int height) throws SQLException {
        super();
        this.studentMetaData = studentMetaData;
        this.assignmentMetaData = assignmentMetaData;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        this.setEnabled(false);

        JLabel nameLabel = new JLabel(assignmentMetaData.getName());

        JPanel gradeFractionPanel = new JPanel();
        gradeFractionPanel.setLayout(new BoxLayout(gradeFractionPanel, BoxLayout.X_AXIS));
        gradeFractionPanel.setOpaque(false);
        gradeFractionPanel.setToolTipText("Add or edit the score this student earned on this assignment");

        // Get student's grade for the assignment and make it editable
        StudentGrade studentGrade = studentMetaData.getGradeForAssignment(assignmentMetaData);
        EditableTextField editableStudentGrade = new EditableStudentGrade(studentGrade, studentMetaData, parentPage);

        // Get the assignment's max grade to display along side the student's grade
        AssignmentWeight assignmentWeight = assignmentMetaData.getWeightForStudentType(studentMetaData.getStudentType());
        JLabel maxGradeLabel = new JLabel("/" + assignmentWeight.getMaxGrade().toString());

        gradeFractionPanel.add(editableStudentGrade);
        gradeFractionPanel.add(maxGradeLabel);

        // Editable assignment weight which creates weight exceptions for the student
        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new BoxLayout(weightPanel, BoxLayout.X_AXIS));
        weightPanel.setOpaque(false);
        weightPanel.setToolTipText("Editing the assignment weight here will only effect this student and not others");
        JLabel weightLabel = new JLabel("Weight: ");
        EditableTextField weightTextField = new EditableAssignmentWeight(studentMetaData, assignmentMetaData, parentPage);
        JLabel percentLabel = new JLabel("%");
        weightPanel.add(weightLabel);
        weightPanel.add(weightTextField);
        weightPanel.add(percentLabel);


        GridBagConstraints nameGBC = new GridBagConstraints();
        nameGBC.anchor = GridBagConstraints.WEST;
        nameGBC.weightx = 1.0;
        nameGBC.gridx = 0;

        GridBagConstraints gradeFractionGBC = new GridBagConstraints();
        gradeFractionGBC.anchor = GridBagConstraints.CENTER;
        gradeFractionGBC.weightx = .2;
        gradeFractionGBC.gridx = 1;

        GridBagConstraints weightGBC = new GridBagConstraints();
        weightGBC.anchor = GridBagConstraints.EAST;
        weightGBC.weightx = .2;
        weightGBC.gridx = 2;

        add(nameLabel, nameGBC);
        add(gradeFractionPanel, gradeFractionGBC);
        add(weightPanel, weightGBC);
    }
}
