package gui;

import assignments.AssignmentMetaData;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AssignmentBox extends JButton {

    private final AssignmentMetaData assignmentMetaData;
    private final StudentType studentType;

    public AssignmentBox(AssignmentMetaData assignmentMetaData, StudentType studentType, int width, int height) throws SQLException {
        super();
        this.assignmentMetaData = assignmentMetaData;
        this.studentType = studentType;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));

        JLabel nameLabel = new JLabel(assignmentMetaData.getName());

        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new BoxLayout(weightPanel, BoxLayout.X_AXIS));
        weightPanel.setOpaque(false);
        JLabel weightLabel = new JLabel("Weight: ");
//                assignmentMetaData.getWeightForStudentType(studentType).getWeightPercent()));
        EditableTextField weightTextField = new EditableWeight(assignmentMetaData, studentType);
        JLabel percentLabel = new JLabel("%");
        weightPanel.add(weightLabel);
        weightPanel.add(weightTextField);
        weightPanel.add(percentLabel);

        GridBagConstraints nameGBC = new GridBagConstraints();
//        nameGBC.anchor = GridBagConstraints.WEST;
        nameGBC.weightx = 1.0;
        nameGBC.gridx = 0;

        GridBagConstraints weightGBC = new GridBagConstraints();
//        weightGBC.anchor = GridBagConstraints.EAST;
        weightGBC.weightx = 1.0;
        weightGBC.gridx = 1;

        add(nameLabel, nameGBC);
        add(weightPanel, weightGBC);
    }

    public AssignmentMetaData getAssignmentMetaData() {
        return assignmentMetaData;
    }
}
