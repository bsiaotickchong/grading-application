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
        JTextField firstName = new EditableStudentFirstName(studentMetaData, this);
        JTextField middleInitial = new EditableStudentMiddleInitial(studentMetaData, this);
        JTextField lastName = new EditableStudentLastName(studentMetaData, this);

        JPanel editableFullName = new JPanel();
        editableFullName.setLayout(new BoxLayout(editableFullName, BoxLayout.X_AXIS));

        editableFullName.add(firstName);
        editableFullName.add(middleInitial);
        editableFullName.add(lastName);

        firstName.setFont(new Font(firstName.getFont().getName(), Font.BOLD, titleFontSize));
        middleInitial.setFont(new Font(middleInitial.getFont().getName(), Font.BOLD, titleFontSize));
        lastName.setFont(new Font(lastName.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        JButton backButton = new BackButton();

        JButton addCategoryButton = new AddCategoryButton(courseMetaData);

        GridBagConstraints fullNameGBC = new GridBagConstraints();
        fullNameGBC.anchor = GridBagConstraints.WEST;
        fullNameGBC.gridx = 0;
        fullNameGBC.gridy = 0;

        GridBagConstraints descriptionGBC = new GridBagConstraints();
        descriptionGBC.gridwidth = GridBagConstraints.REMAINDER;
        descriptionGBC.fill = GridBagConstraints.HORIZONTAL;
        descriptionGBC.gridx = 0;
        descriptionGBC.gridy = 1;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 2;

        GridBagConstraints addCategoryGBC = new GridBagConstraints();
        addCategoryGBC.gridx = 0;
        addCategoryGBC.gridy = 2;

        add(editableFullName, fullNameGBC);
        add(description, descriptionGBC);
        add(backButton, backButtonGBC);
        add(addCategoryButton, addCategoryGBC);
    }
}
