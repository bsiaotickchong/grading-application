package gui;

import gui.Pages.Page;
import students.StudentMetaData;

import java.sql.SQLException;

public class EditableStudentLastName extends EditableTextField {

    private final StudentMetaData studentMetaData;

    public EditableStudentLastName(StudentMetaData studentMetaData,
                                   Page parentPage) {
        super(studentMetaData.getLastName(), parentPage);
        this.studentMetaData = studentMetaData;
        this.setToolTipText("Last name");
    }

    @Override
    void updateText(String updatedText) throws SQLException {
        studentMetaData.setLastName(updatedText);

        getParentPage().redrawPage();
    }
}
