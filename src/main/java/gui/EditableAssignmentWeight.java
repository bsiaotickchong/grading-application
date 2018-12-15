package gui;

import assignments.AssignmentMetaData;
import gui.Pages.AssignmentPage;
import gui.Pages.CoursePage;
import gui.Pages.Page;
import gui.Pages.StudentPage;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import java.awt.*;
import java.sql.SQLException;

public class EditableAssignmentWeight extends EditableTextField {

    private final static Logger LOG = LoggerFactory.getLogger(EditableAssignmentWeight.class);
    private final static String WEIGHT_TOOLTIP = "Edit and click out of this box to change the weight";
    private final static String WEIGHT_EXCEPTION_TOOLTIP = "This assignment weight is an exception and applies only to this student";

    private AssignmentMetaData assignmentMetaData;
    private StudentType studentType;
    private StudentMetaData studentMetaData;

    private EditableAssignmentWeight(Double weight,
                                     AssignmentMetaData assignmentMetaData,
                                     StudentType studentType,
                                     Page parentPage) {
        super(weight.toString(), parentPage);

        this.assignmentMetaData = assignmentMetaData;
        this.studentType = studentType;
    }

    public EditableAssignmentWeight(AssignmentMetaData assignmentMetaData,
                                    StudentType studentType,
                                    Page parentPage) throws SQLException {
        this(assignmentMetaData.getWeightForStudentType(studentType).getWeightPercent(),
                assignmentMetaData,
                studentType,
                parentPage);

        this.setToolTipText(WEIGHT_TOOLTIP);
    }

    public EditableAssignmentWeight(StudentMetaData studentMetaData,
                                    AssignmentMetaData assignmentMetaData,
                                    Page parentPage) throws SQLException {
        this(assignmentMetaData.getWeightForStudent(studentMetaData),
                assignmentMetaData,
                studentMetaData.getStudentType(),
                parentPage);

        this.studentMetaData = studentMetaData;

        if (assignmentMetaData.hasWeightExceptionForStudent(studentMetaData)) {
            this.setBackground(Color.YELLOW);
            this.setToolTipText(WEIGHT_EXCEPTION_TOOLTIP);
        } else {
            this.setToolTipText(WEIGHT_TOOLTIP);
        }
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

        // if this editable weight object was created for a CategoriesAndAssignmentsPanel in a CoursePage,
        // be sure to update the weight total shown in that panel
        if (getParentPage() instanceof CoursePage) {
            assignmentMetaData.setWeightForStudentType(studentType, updatedWeight);
            CategoriesAndAssignmentsPanel categoriesAndAssignmentsPanel = ((CoursePage) getParentPage()).getCategoriesAndAssignmentsPanel();
            categoriesAndAssignmentsPanel.updateWeightTotal();
            this.setBackground(Color.WHITE);

            LOG.info("Assignment ('{}') weight updated to {}%",
                    assignmentMetaData.getName(),
                    assignmentMetaData.getWeightForStudentType(studentType).getWeightPercent());
        } else if (getParentPage() instanceof StudentPage || getParentPage() instanceof AssignmentPage) {
            // create assignment weight exception
            assignmentMetaData.setWeightExceptionForStudent(studentMetaData, updatedWeight);
            this.setBackground(Color.YELLOW);
            this.setToolTipText(WEIGHT_EXCEPTION_TOOLTIP);

            LOG.info("Assignment ('{}') weight exception set to {}%. Original assignment weight was {}%",
                    assignmentMetaData.getName(),
                    assignmentMetaData.getWeightForStudent(studentMetaData),
                    assignmentMetaData.getWeightForStudentType(studentType).getWeightPercent());
        } else {
            LOG.error("The weight was not set in the database. Add your own else if statement for the Page you add this component to");
        }

        getParentPage().redrawPage();
    }
}
