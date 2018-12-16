package gui.assignments;

import assignments.AssignmentMetaData;
import courses.CourseMetaData;
import gui.editables.EditableAssignmentWeight;
import gui.editables.EditableStudentGrade;
import gui.editables.EditableTextField;
import gui.pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.AssignmentWeight;
import org.jooq.grading_app.db.h2.tables.pojos.StudentGrade;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class GradesStudentAssignmentBox extends JButton {

    private final AssignmentMetaData assignmentMetaData;
    private final StudentType studentType;
    private final StudentMetaData studentMetaData;

    public GradesStudentAssignmentBox(AssignmentMetaData assignmentMetaData,
                         StudentMetaData studentMetaData,
                         StudentType studentType,
                         Page parentPage,
                         int width,
                         int height) throws SQLException {
        super();
        this.assignmentMetaData = assignmentMetaData;
        this.studentType = studentType;
        this.studentMetaData = studentMetaData;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));

        JLabel nameLabel = new JLabel(studentMetaData.getFullName());

        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new BoxLayout(weightPanel, BoxLayout.X_AXIS));
        weightPanel.setOpaque(false);
        JLabel weightLabel = new JLabel("Weight: ");

        Component weightTextField;
        if (studentType != null) { // this null check is a necessary evil due to time
            weightTextField = new EditableAssignmentWeight(studentMetaData, assignmentMetaData, parentPage);
        } else {
            weightTextField = new JLabel("N/A");
            ((JLabel) weightTextField).setToolTipText("Weight is only editable for a specific student type once students are enrolled");
            weightTextField.setBackground(Color.GRAY);
        }

        JLabel percentLabel = new JLabel("%");
        weightPanel.add(weightLabel);
        weightPanel.add(weightTextField);
        weightPanel.add(percentLabel);

        JLabel schoolIdLabel = new JLabel(String.valueOf(studentMetaData.getSchoolId()));

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

        GridBagConstraints nameGBC = new GridBagConstraints();
        nameGBC.anchor = GridBagConstraints.WEST;
        nameGBC.weightx = 1.0;
        nameGBC.gridx = 0;

        GridBagConstraints weightGBC = new GridBagConstraints();
        weightGBC.anchor = GridBagConstraints.EAST;
        weightGBC.weightx = .2;
        weightGBC.gridx = 1;

        GridBagConstraints schoolIdGBC = new GridBagConstraints();
        schoolIdGBC.anchor = GridBagConstraints.EAST;
        schoolIdGBC.weightx = 1.0;
        schoolIdGBC.gridx = 2;

        GridBagConstraints gradeGBC = new GridBagConstraints();
        gradeGBC.anchor = GridBagConstraints.EAST;
        gradeGBC.weightx = 1.0;
        gradeGBC.gridx = 3;

        add(nameLabel, nameGBC);
        add(weightPanel, weightGBC);
        add(schoolIdLabel, schoolIdGBC);
        add(gradeFractionPanel, gradeGBC);
    }

    public AssignmentMetaData getAssignmentMetaData() {
        return assignmentMetaData;
    }

    public StudentMetaData getStudentMetaData(){
        return  studentMetaData;
    }

    public CourseMetaData getCourseMetaData() {
        return assignmentMetaData.getCourseMetaData();
    }
}
