package gui.Pages;

import courses.CourseMetaData;
import gui.*;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;

public class StudentPage extends Page {


    private final StudentMetaData studentMetaData;
    private final CourseMetaData courseMetaData;

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

        JLabel gradesTitle = new JLabel("Assignment Grades");
        gradesTitle.setFont(new Font(gradesTitle.getFont().getName(), Font.BOLD, 16));

        JScrollPane studentAssignmentList = new StudentAssignmentList(studentMetaData, courseMetaData, this, studentAssignmentListWidth);
        studentAssignmentList.setPreferredSize(new Dimension(studentAssignmentListWidth, studentAssignmentListHeight));

        JScrollPane studentNoteList = new StudentNoteList(studentMetaData, courseMetaData, this, studentAssignmentListWidth);

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

        GridBagConstraints gradesTitleGBC = new GridBagConstraints();
        gradesTitleGBC.anchor = GridBagConstraints.WEST;
        gradesTitleGBC.gridx = 0;
        gradesTitleGBC.gridy = 2;
        gradesTitleGBC.ipady = 50;

        GridBagConstraints studentAssignmentListGBC = new GridBagConstraints();
        studentAssignmentListGBC.gridx = 0;
        studentAssignmentListGBC.gridy = 3;
        GridBagConstraints studentNoteListGBC = new GridBagConstraints();
        studentNoteListGBC.gridx = 1;
        studentNoteListGBC.gridy = 3;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;


        add(editableFullName, fullNameGBC);
        add(description, descriptionGBC);
        add(gradesTitle, gradesTitleGBC);
        add(studentAssignmentList, studentAssignmentListGBC);
        add(studentNoteList, studentNoteListGBC);
        add(backButton, backButtonGBC);
    }
}
