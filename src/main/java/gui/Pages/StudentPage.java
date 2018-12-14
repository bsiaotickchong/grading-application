package gui.Pages;

import courses.CourseMetaData;
import gui.*;
import org.jooq.grading_app.db.h2.tables.pojos.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentPage extends Page {

    private final static Logger LOG = LoggerFactory.getLogger(StudentPage.class);

    private final StudentMetaData studentMetaData;
    private final CourseMetaData courseMetaData;
    private JScrollPane studentNoteList;

    public StudentPage(StudentMetaData studentMetaData,
                       CourseMetaData courseMetaData) {
        super("Student Page", "Student information and grades for course " + courseMetaData.getName());
        this.studentMetaData = studentMetaData;
        this.courseMetaData = courseMetaData;
    }

    @Override
    public void loadPage() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int studentAssignmentListWidth = screenSize.width/4;
        int studentAssignmentListHeight = screenSize.height/2;
        int studentNoteListWidth = screenSize.width/4;
        int studentNoteListHeight = screenSize.height/2;

        JTextField firstName = new EditableStudentFirstName(studentMetaData, this);
        JTextField middleInitial = new EditableStudentMiddleInitial(studentMetaData, this);
        JTextField lastName = new EditableStudentLastName(studentMetaData, this);

        JPanel editableFullName = new JPanel();
        editableFullName.setLayout(new BoxLayout(editableFullName, BoxLayout.X_AXIS));
        editableFullName.setOpaque(false);

        editableFullName.add(firstName);
        editableFullName.add(middleInitial);
        editableFullName.add(lastName);

        firstName.setFont(new Font(firstName.getFont().getName(), Font.BOLD, titleFontSize));
        middleInitial.setFont(new Font(middleInitial.getFont().getName(), Font.BOLD, titleFontSize));
        lastName.setFont(new Font(lastName.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        // Display student information
        JPanel studentInfoPanel = getStudentInfoPanel();

        // Display assignments
        JLabel gradesTitle = new JLabel("Assignment Grades");
        gradesTitle.setFont(new Font(gradesTitle.getFont().getName(), Font.BOLD, 16));

        JScrollPane studentAssignmentList = new StudentAssignmentList(studentMetaData, courseMetaData, this, studentAssignmentListWidth);
        studentAssignmentList.setPreferredSize(new Dimension(studentAssignmentListWidth, studentAssignmentListHeight));

        // Display notes
        JLabel notesTitle = new JLabel("Student Notes");
        notesTitle.setFont(new Font(notesTitle.getFont().getName(), Font.BOLD, 16));

        studentNoteList = new StudentNoteList(studentMetaData, courseMetaData, this, studentAssignmentListWidth);
        studentNoteList.setPreferredSize(new Dimension(studentNoteListWidth, studentNoteListHeight));

        JButton backButton = new BackButton();

        GridBagConstraints fullNameGBC = new GridBagConstraints();
        fullNameGBC.anchor = GridBagConstraints.WEST;
        fullNameGBC.gridx = 0;
        fullNameGBC.gridy = 0;

        GridBagConstraints descriptionGBC = new GridBagConstraints();
        descriptionGBC.gridwidth = GridBagConstraints.REMAINDER;
        descriptionGBC.fill = GridBagConstraints.HORIZONTAL;
        descriptionGBC.gridx = 0;
        descriptionGBC.gridy = 1;

        GridBagConstraints studentInfoGBC = new GridBagConstraints();
        studentInfoGBC.gridheight = 4;
        studentInfoGBC.anchor = GridBagConstraints.NORTHEAST;
        studentInfoGBC.gridx = 2;
        studentInfoGBC.gridy = 0;
        studentInfoGBC.ipadx = 50;

        GridBagConstraints gradesTitleGBC = new GridBagConstraints();
        gradesTitleGBC.anchor = GridBagConstraints.WEST;
        gradesTitleGBC.gridx = 0;
        gradesTitleGBC.gridy = 2;
        gradesTitleGBC.ipady = 50;

        GridBagConstraints notesTitleGBC = new GridBagConstraints();
        notesTitleGBC.anchor = GridBagConstraints.WEST;
        notesTitleGBC.gridx = 1;
        notesTitleGBC.gridy = 2;
        notesTitleGBC.ipady = 50;

        GridBagConstraints studentAssignmentListGBC = new GridBagConstraints();
        studentAssignmentListGBC.gridx = 0;
        studentAssignmentListGBC.gridy = 3;
        GridBagConstraints studentNoteListGBC = new GridBagConstraints();
        studentNoteListGBC.gridx = 1;
        studentNoteListGBC.gridy = 3;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.anchor = GridBagConstraints.WEST;
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;


        add(editableFullName, fullNameGBC);
        add(description, descriptionGBC);
        add(studentInfoPanel, studentInfoGBC);
        add(gradesTitle, gradesTitleGBC);
        add(notesTitle, notesTitleGBC);
        add(studentAssignmentList, studentAssignmentListGBC);
        add(studentNoteList, studentNoteListGBC);
        add(backButton, backButtonGBC);
    }

    private JPanel getStudentInfoPanel() {
        JPanel studentInfoPanel = new JPanel();
        studentInfoPanel.setLayout(new BoxLayout(studentInfoPanel, BoxLayout.Y_AXIS));
        studentInfoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        studentInfoPanel.setBackground(Color.WHITE);

        studentInfoPanel.add(new JLabel("Email: " + studentMetaData.getEmail()));
        studentInfoPanel.add(new JLabel("ID: " + "<id goes here>"));
        studentInfoPanel.add(new JLabel("Student type: " + studentMetaData.getStudentType().getName()));
        studentInfoPanel.add(new JLabel("Current grade: " + "<grade goes here>"));

        JPanel coursesPanel;
        try {
            coursesPanel = getCoursesPanel();
            studentInfoPanel.add(coursesPanel);
        } catch (SQLException e) {
            LOG.error("Could not create courses panel: {}", e.getMessage());
        }

        return studentInfoPanel;
    }

    private JPanel getCoursesPanel() throws SQLException {
        JPanel coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBackground(Color.WHITE);

        coursesPanel.add(new JLabel("Courses enrolled in:"));

        for (Course course : studentMetaData.getCourses()) {
            JButton studentCourseButton = new StudentCourseButton(
                    studentMetaData,
                    new CourseMetaData(course),
                    100,
                    50);
            coursesPanel.add(studentCourseButton);

            if (course.getId() == courseMetaData.getId()) {
                studentCourseButton.setEnabled(false);
            }
        }

        return coursesPanel;
    }

    @Override
    public void redrawPage() {
        ((StudentNoteList) this.studentNoteList).updateNoteList();
        super.redrawPage();
    }
}
