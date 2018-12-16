package gui.assignments;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import application.GradingApplication;
import assignments.AssignmentMetaData;
import courses.CourseMetaData;
import gui.pages.Page;
import gui.pages.StudentPage;
import org.jooq.grading_app.db.h2.tables.pojos.Student;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import java.util.List;

public class AssignmentStudentList extends JScrollPane implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AssignmentStudentList.class);

    private final Page parentPage;
    private final CourseMetaData courseMetaData;
    private final int width;

    public AssignmentStudentList(StudentType s,
                                 Page parentPage,
                                 CourseMetaData courseMetaData,
                                 AssignmentMetaData assignmentMetaData,
                                 int width){
        super(new JPanel());
        this.parentPage = parentPage;
        this.courseMetaData = courseMetaData;
        this.width = width;

        JPanel studentListPanel = (JPanel) this.getViewport().getView();
        studentListPanel.setLayout(new BoxLayout(studentListPanel, BoxLayout.Y_AXIS));

        try {
            populateListWithStudents(studentListPanel, assignmentMetaData,  s);
        } catch(Exception e){
            JTextArea textArea = new JTextArea(5, 20);
            textArea.setEditable(false);
            textArea.append("Error loading Assignment Students List");
            e.printStackTrace();
        }
    }

    private void populateListWithStudents(JPanel studentListPanel, AssignmentMetaData assignmentMetaData, StudentType studentType) throws SQLException{

        List<Student> students = courseMetaData.getEnrolledStudents();

        for (Student student : students) {

            if(studentType != null) {
                if (!student.getStudentTypeId().equals(studentType.getId())) {
                    continue;
                }
            }

            StudentMetaData studentMetaData = new StudentMetaData(student);
            GradesStudentAssignmentBox assignmentBox = new GradesStudentAssignmentBox(assignmentMetaData, studentMetaData, studentMetaData.getStudentType(), parentPage,200, 50);
            studentListPanel.add(assignmentBox);
            assignmentBox.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        GradesStudentAssignmentBox studentAssignmentBox = (GradesStudentAssignmentBox) e.getSource();
        studentAssignmentBox.getAssignmentMetaData().printMetaData();

        GradingApplication.PAGE_LOADER.loadNewPage(new StudentPage(studentAssignmentBox.getStudentMetaData(), studentAssignmentBox.getCourseMetaData()));
    }

}
