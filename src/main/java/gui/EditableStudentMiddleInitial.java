package gui;

import gui.Pages.Page;
import students.StudentMetaData;

import java.awt.*;
import java.sql.SQLException;

public class EditableStudentMiddleInitial extends EditableTextField {

    private final StudentMetaData studentMetaData;

    public EditableStudentMiddleInitial(StudentMetaData studentMetaData,
                                        Page parentPage) {
        super(studentMetaData.getMiddleInitial(), parentPage);
        this.studentMetaData = studentMetaData;
        this.setPreferredSize(new Dimension(40,0));
        this.setToolTipText("Middle initial");
    }

    @Override
    void updateText(String updatedText) throws SQLException {
        studentMetaData.setMiddleInitial(updatedText);

        getParentPage().redrawPage();
    }
}
