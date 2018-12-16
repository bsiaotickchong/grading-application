package gui.students;

import application.GradingApplication;
import courses.CourseMetaData;
import gui.pages.Page;
import gui.pages.StudentPage;
import gui.buttons.AddStudentButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentList extends JScrollPane implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(StudentList.class);

    private final List<StudentMetaData> studentMetaDatas;
    private final int width;
    private final Page parentPage;
    private final CourseMetaData courseMetaData;

    public StudentList(List<StudentMetaData> studentMetaDatas,
                       CourseMetaData courseMetaData,
                       Page parentPage,
                       int width) {
        super(new JPanel());
        this.studentMetaDatas = studentMetaDatas;
        this.courseMetaData = courseMetaData;
        this.parentPage = parentPage;
        this.width = width;

        JPanel studentListPanel = (JPanel) this.getViewport().getView();
        studentListPanel.setLayout(new BoxLayout(studentListPanel, BoxLayout.Y_AXIS));

        try {
            populateListWithStudents(studentListPanel);
        } catch (Exception e) {
            LOG.error("Could not create student list: {}", e.getMessage());
        }

        JButton addStudentButton = new AddStudentButton(courseMetaData);
        studentListPanel.add(addStudentButton);
    }

    private void populateListWithStudents(JPanel studentListPanel) {
        for (StudentMetaData studentMetaData : studentMetaDatas) {
            JButton studentBox = new StudentBox(studentMetaData, parentPage, width-20, 50);

            studentListPanel.add(studentBox);
            studentBox.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StudentBox studentBox = (StudentBox) e.getSource();

        GradingApplication.PAGE_LOADER.loadNewPage(new StudentPage(studentBox.getStudentMetaData(), courseMetaData));
    }
}
