package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import application.GradingApplication;
import assignments.AssignmentsMetaData;
import assignments.AssignmentsMetaDataManager;
import courses.CourseMetaData;
import database.MetaData;
import org.jooq.grading_app.db.h2.tables.Assignment;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

public class AssignmentStudentList extends JScrollPane implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AssignmentStudentList.class);

    public AssignmentStudentList(JPanel studentListPanel, StudentType s){
        super(studentListPanel);

        setBorder(BorderFactory.createLineBorder(Color.black));
        studentListPanel.setLayout(new BoxLayout(studentListPanel, BoxLayout.Y_AXIS));

        try {
            poplateListWithStudents(studentListPanel, s);
        }catch(Exception e){
            JTextArea textArea = new JTextArea(5, 20);
            textArea.setEditable(false);
            textArea.append("Error loading Assignment Students List");
            e.printStackTrace();
        }

    }

    private void poplateListWithStudents(JPanel studentListPanel, StudentType studentType) throws SQLException{
        AssignmentsMetaDataManager assignmentsMetaDataManager = new AssignmentsMetaDataManager();

        List<MetaData> assignmentsMetaDatas = assignmentsMetaDataManager.getAllMetaData();

        for (MetaData assignMetaData : assignmentsMetaDatas){
            AssignmentBox assignmentBox = new AssignmentBox((AssignmentsMetaData) assignMetaData, (StudentType) studentType, 200, 50);
            studentListPanel.add(assignmentBox);
            assignmentBox.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        AssignmentBox assignmentBox = (AssignmentBox) e.getSource();
        assignmentBox.getAssignmentMetaData().printMetaData();

        GradingApplication.PAGE_LOADER.loadNewPage(new AssignmentPage(assignmentBox.getAssignmentMetaData(), assignmentBox.getStudentType()));
    }

}
