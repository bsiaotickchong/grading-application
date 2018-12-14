package gui;

import assignments.AssignmentMetaData;
import courses.CourseMetaData;
import gui.Pages.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class StudentAssignmentList extends JScrollPane {

    private final static Logger LOG = LoggerFactory.getLogger(StudentAssignmentList.class);

    private final StudentMetaData studentMetaData;
    private final CourseMetaData courseMetaData;
    private final Page parentPage;
    private final int width;

    public StudentAssignmentList(StudentMetaData studentMetaData,
                                 CourseMetaData courseMetaData,
                                 Page parentPage,
                                 int width) {
        super(new JPanel());
        this.studentMetaData = studentMetaData;
        this.courseMetaData = courseMetaData;
        this.parentPage = parentPage;
        this.width = width;

        JPanel studentAssignmentListPanel = (JPanel) this.getViewport().getView();
        studentAssignmentListPanel.setLayout(new BoxLayout(studentAssignmentListPanel, BoxLayout.Y_AXIS));

        try {
            populateListWithAssignments(studentAssignmentListPanel);
        } catch (Exception e) {
            LOG.error("Couldn't create StudentAssignmentList {}", e.getMessage());
        }
    }

    private void populateListWithAssignments(JPanel studentAssignmentListPanel) throws SQLException {
        List<AssignmentMetaData> assignmentMetaDatas = courseMetaData.getAllAssignmentMetaDatas();

        for (AssignmentMetaData assignmentMetaData : assignmentMetaDatas) {
            JButton studentAssignmentBox = new StudentAssignmentBox(studentMetaData, assignmentMetaData, parentPage, width-20, 50);
            studentAssignmentListPanel.add(studentAssignmentBox);
        }
    }

    // TODO: have weight exception RESET button which deletes all assignment weight exceptions for this student

}
