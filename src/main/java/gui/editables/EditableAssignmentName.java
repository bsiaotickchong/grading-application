package gui.editables;

import assignments.AssignmentMetaData;
import gui.pages.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class EditableAssignmentName extends EditableTextField {

    private final static Logger LOG = LoggerFactory.getLogger(EditableAssignmentName.class);

    private final AssignmentMetaData assignmentMetaData;

    public EditableAssignmentName(AssignmentMetaData assignmentMetaData, Page parentPage) throws SQLException {
        super(assignmentMetaData.getName(), parentPage);
        this.assignmentMetaData = assignmentMetaData;

        this.setToolTipText("Edit and click out of this box to change the assignment name");
    }

    @Override
    void updateText(String updatedText) throws SQLException {
        LOG.debug("Clicked out with value: {}", updatedText);

        assignmentMetaData.setAssignmentName(updatedText);

        getParentPage().redrawPage();
    }
}
