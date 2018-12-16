package gui.editables;

import gui.pages.Page;
import students.StudentMetaData;

import java.sql.SQLException;

public class EditableStudentFirstName extends EditableTextField {

    private final StudentMetaData studentMetaData;

    public EditableStudentFirstName(StudentMetaData studentMetaData,
                                    Page parentPage) {
        super(studentMetaData.getFirstName(), parentPage);
        this.studentMetaData = studentMetaData;
        this.setToolTipText("First name");
    }

    @Override
    void updateText(String updatedText) throws SQLException {
        studentMetaData.setFirstName(updatedText);

        getParentPage().redrawPage();
    }
}
