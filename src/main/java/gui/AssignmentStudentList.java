package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import application.GradingApplication;
import assignments.AssignmentMetaData;
import gui.Pages.AssignmentPage;
import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import java.util.List;

public class AssignmentStudentList extends JScrollPane implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AssignmentStudentList.class);

    private final Page parentPage;

    public AssignmentStudentList(JPanel studentListPanel,
                                 StudentType s,
                                 Page parentPage,
                                 AssignmentMetaData assignmentMetaData){
        super(studentListPanel);
        this.parentPage = parentPage;

        setBorder(BorderFactory.createLineBorder(Color.black));
        studentListPanel.setLayout(new BoxLayout(studentListPanel, BoxLayout.Y_AXIS));

        try {
            poplateListWithStudents(studentListPanel, assignmentMetaData,  s);
        }catch(Exception e){
            JTextArea textArea = new JTextArea(5, 20);
            textArea.setEditable(false);
            textArea.append("Error loading Assignment Students List");
            e.printStackTrace();
        }

    }

    private void poplateListWithStudents(JPanel studentListPanel, AssignmentMetaData assignmentMetaData, StudentType studentType) throws SQLException{

        List<StudentMetaData> studentsMetaDatas = assignmentMetaData.getStudentsByStudentType(studentType);

        for (StudentMetaData studentMetaData : studentsMetaDatas){
            GradesStudentAssignmentBox assignmentBox = new GradesStudentAssignmentBox(assignmentMetaData, studentMetaData, studentType, parentPage,200, 50);
            studentListPanel.add(assignmentBox);
            assignmentBox.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        GradesStudentAssignmentBox assignmentBox = (GradesStudentAssignmentBox) e.getSource();
        assignmentBox.getAssignmentMetaData().printMetaData();

        GradingApplication.PAGE_LOADER.loadNewPage(new AssignmentPage(assignmentBox.getAssignmentMetaData(), assignmentBox.getStudentType()));
    }

}
