package gui;

import assignments.AssignmentMetaData;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.SQLException;

public class EditableWeight extends EditableTextField {

    private final static Logger LOG = LoggerFactory.getLogger(EditableWeight.class);

    private AssignmentMetaData assignmentMetaData;
    private StudentType studentType;
    private JPanel parentPanel;

    public EditableWeight(AssignmentMetaData assignmentMetaData,
                          StudentType studentType) throws SQLException {
        super(assignmentMetaData.getWeightForStudentType(studentType).getWeightPercent().toString());
        this.assignmentMetaData = assignmentMetaData;
        this.studentType = studentType;

        this.setToolTipText("Edit and click out of this box to change the weight!");
    }

    public EditableWeight(AssignmentMetaData assignmentMetaData,
                          StudentType studentType,
                          JPanel parentPanel) throws SQLException {
        this(assignmentMetaData, studentType);
        this.parentPanel = parentPanel;
    }

    @Override
    void updateText(String updatedString) throws SQLException {
        LOG.debug("Clicked out with value: {}", this.getText());

        double updatedWeight;
        if (this.getText().isEmpty()) {
            updatedWeight = 0;
            this.setText("0.0");
        } else {
            updatedWeight = Double.parseDouble(this.getText());
        }

        assignmentMetaData.setWeightForStudentType(studentType, updatedWeight);

        // if this editable weight object was created for a CategoriesAndAssignmentsPanel,
        // be sure to update the weight total shown in that panel
        if (parentPanel instanceof CategoriesAndAssignmentsPanel) {
            CategoriesAndAssignmentsPanel categoriesAndAssignmentsPanel = (CategoriesAndAssignmentsPanel) parentPanel;
            categoriesAndAssignmentsPanel.updateWeightTotal();
            categoriesAndAssignmentsPanel.redrawPanel();
        }
    }
}
