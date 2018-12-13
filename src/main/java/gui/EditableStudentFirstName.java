package gui;

import gui.Pages.Page;
import students.StudentMetaData;

import javax.swing.*;
import java.sql.SQLException;

public class EditableStudentFirstName extends EditableTextField {

    private final StudentMetaData studentMetaData;

    public EditableStudentFirstName(StudentMetaData studentMetaData,
                                    Page parentPage) {
        super(studentMetaData.getFirstName(), parentPage);
        this.studentMetaData = studentMetaData;
    }

    @Override
    void updateText(String updatedText) throws SQLException {
        studentMetaData.setFirstName(updatedText);

        getParentPage().redrawPage();
    }
}
