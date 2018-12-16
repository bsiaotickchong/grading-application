package gui.assignments;

import assignments.AssignmentMetaData;
import gui.editables.EditableAssignmentName;
import gui.editables.EditableAssignmentWeight;
import gui.editables.EditableTextField;
import gui.pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AssignmentBox extends JButton {

    private final AssignmentMetaData assignmentMetaData;
    private final StudentType studentType;

    public AssignmentBox(AssignmentMetaData assignmentMetaData,
                         StudentType studentType,
                         Page parentPage,
                         int width,
                         int height) throws SQLException {
        super();
        this.assignmentMetaData = assignmentMetaData;
        this.studentType = studentType;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));

        EditableTextField nameLabel = new EditableAssignmentName(assignmentMetaData, parentPage);

        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new BoxLayout(weightPanel, BoxLayout.X_AXIS));
        weightPanel.setOpaque(false);
        JLabel weightLabel = new JLabel("Weight: ");

        Component weightTextField;
        if (studentType != null) { // this null check is a necessary evil due to time
            weightTextField = new EditableAssignmentWeight(assignmentMetaData, studentType, parentPage);
        } else {
            weightTextField = new JLabel("N/A");
            ((JLabel) weightTextField).setToolTipText("Weight is only editable for a specific student type once students are enrolled");
            weightTextField.setBackground(Color.GRAY);
        }

        JLabel percentLabel = new JLabel("%");
        weightPanel.add(weightLabel);
        weightPanel.add(weightTextField);
        weightPanel.add(percentLabel);

        GridBagConstraints nameGBC = new GridBagConstraints();
        nameGBC.anchor = GridBagConstraints.WEST;
        nameGBC.weightx = 1.0;
        nameGBC.gridx = 0;

        GridBagConstraints weightGBC = new GridBagConstraints();
        weightGBC.anchor = GridBagConstraints.EAST;
        weightGBC.weightx = .2;
        weightGBC.gridx = 1;

        add(nameLabel, nameGBC);
        add(weightPanel, weightGBC);
    }

    public AssignmentMetaData getAssignmentMetaData() {
        return assignmentMetaData;
    }
}
