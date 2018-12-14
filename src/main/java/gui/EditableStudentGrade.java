package gui;

import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.StudentGrade;
import students.StudentMetaData;

import java.sql.SQLException;

public class EditableStudentGrade extends EditableTextField {

    private StudentGrade studentGrade;
    private StudentMetaData studentMetaData;

    public EditableStudentGrade(StudentGrade studentGrade,
                                StudentMetaData studentMetaData,
                                Page parentPage) {
        super(studentGrade.getGrade().toString(), parentPage);
        this.studentGrade = studentGrade;
        this.studentMetaData = studentMetaData;
    }

    @Override
    void updateText(String updatedText) throws SQLException {
        studentGrade.setGrade(Double.parseDouble(updatedText));
        studentMetaData.setGrade(studentGrade);
        getParentPage().redrawPage();
    }
}
