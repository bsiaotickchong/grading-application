package gui;

import assignments.AssignmentMetaData;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;

import java.sql.SQLException;

public class EditableWeight extends EditableTextField {

    private AssignmentMetaData assignmentMetaData;
    private StudentType studentType;

    public EditableWeight(AssignmentMetaData assignmentMetaData,
                          StudentType studentType) throws SQLException {
        super(assignmentMetaData.getWeightForStudentType(studentType).getWeightPercent().toString());
        this.assignmentMetaData = assignmentMetaData;
    }

    @Override
    void updateText() {

    }
}
