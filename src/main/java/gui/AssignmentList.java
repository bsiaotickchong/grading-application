package gui;

import application.GradingApplication;
import assignments.AssignmentMetaData;
import courses.CourseMetaData;
import database.MetaData;
import gui.Pages.AssignmentPage;
import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.Category;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class AssignmentList extends JScrollPane implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AssignmentList.class);

    private final StudentType studentType;
    private final List<AssignmentMetaData> assignmentMetaDatas;
    private final int width;
    private final Page parentPage;
    private final CourseMetaData courseMetaData;

    public AssignmentList(List<AssignmentMetaData> assignmentMetaDatas,
                          StudentType studentType,
                          Page parentPage,
                          CourseMetaData courseMetaData,
                          Category category,
                          int width) {
        super(new JPanel());
        this.assignmentMetaDatas = assignmentMetaDatas;
        this.studentType = studentType;
        this.parentPage = parentPage;
        this.courseMetaData = courseMetaData;
        this.width = width;

        JPanel assignmentListPanel = (JPanel) this.getViewport().getView();
        assignmentListPanel.setLayout(new BoxLayout(assignmentListPanel, BoxLayout.Y_AXIS));

        try {
            populateListWithAssignments(assignmentListPanel);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }

        JButton addAssignmentButton = new AddAssignmentButton(courseMetaData, category);
        assignmentListPanel.add(addAssignmentButton);
    }

    private void populateListWithAssignments(JPanel assignmentListPanel) throws SQLException {
        for (MetaData assignmentMetaData : assignmentMetaDatas) {
            JButton assignmentBox = new AssignmentBox((AssignmentMetaData) assignmentMetaData, studentType, parentPage,width-20, 50);

            assignmentListPanel.add(assignmentBox);
            assignmentBox.addActionListener(this);
        }
    }

    public double getTotalWeight() throws SQLException {
        double total = 0;

        for (AssignmentMetaData assignmentMetaData : assignmentMetaDatas) {
            total += assignmentMetaData.getWeightForStudentType(studentType).getWeightPercent();
        }

        return total;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AssignmentBox assignmentBox = (AssignmentBox) e.getSource();

        GradingApplication.PAGE_LOADER.loadNewPage(new AssignmentPage(assignmentBox.getAssignmentMetaData(), courseMetaData, studentType));
    }

    public StudentType getStudentType() {
        return studentType;
    }
}
