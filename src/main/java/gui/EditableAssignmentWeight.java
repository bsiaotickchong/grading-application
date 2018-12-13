package gui;

import assignments.AssignmentMetaData;
import gui.Pages.AssignmentPage;
import gui.Pages.CoursePage;
import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.SQLException;

public class EditableAssignmentWeight extends EditableTextField {

    private final static Logger LOG = LoggerFactory.getLogger(EditableAssignmentWeight.class);

    private AssignmentMetaData assignmentMetaData;
    private StudentType studentType;

    public EditableAssignmentWeight(AssignmentMetaData assignmentMetaData,
                                    StudentType studentType,
                                    Page parentPage) throws SQLException {
        super(assignmentMetaData.getWeightForStudentType(studentType).getWeightPercent().toString(),
                parentPage);
        this.assignmentMetaData = assignmentMetaData;
        this.studentType = studentType;

        this.setToolTipText("Edit and click out of this box to change the weight");
    }

    @Override
    void updateText(String updatedText) throws SQLException {
        LOG.debug("Clicked out with value: {}", updatedText);

        double updatedWeight;
        if (updatedText.isEmpty()) {
            updatedWeight = 0;
            this.setText("0.0");
        } else {
            updatedWeight = Double.parseDouble(updatedText);
        }

        assignmentMetaData.setWeightForStudentType(studentType, updatedWeight);

        // if this editable weight object was created for a CategoriesAndAssignmentsPanel,
        // be sure to update the weight total shown in that panel
        if (getParentPage() instanceof CoursePage) {
            CategoriesAndAssignmentsPanel categoriesAndAssignmentsPanel = ((CoursePage) getParentPage()).getCategoriesAndAssignmentsPanel();
            categoriesAndAssignmentsPanel.updateWeightTotal();
//            categoriesAndAssignmentsPanel.redrawPanel();
            getParentPage().redrawPage();
        }
    }
}
