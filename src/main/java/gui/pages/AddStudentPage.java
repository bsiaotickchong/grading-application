package gui.pages;

import application.GradingApplication;
import courses.CourseMetaData;
import database.H2DatabaseUtil;
import gui.buttons.BackButton;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.Major;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.jooq.grading_app.db.h2.tables.records.MajorRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.jooq.grading_app.db.h2.tables.Major.MAJOR;
import static org.jooq.impl.DSL.selectFrom;

public class AddStudentPage extends Page implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddStudentPage.class);

    private final CourseMetaData courseMetaData;

    private JTextField studentFirstNameTextField;
    private JTextField studentMiddleInitialTextField;
    private JTextField studentSchoolIdTextField;
    private JTextField studentLastNameTextField;
    private JTextField studentEmailTextField;
    private JTextField studentMajorTextField;
    private JTextField studentYearTextField;
    private JComboBox<String> studentTypeCB;
    private List<StudentType> studentTypeList;

    public AddStudentPage(CourseMetaData courseMetaData) {
        super("Add a Student", "Add students to this class.");

        this.courseMetaData = courseMetaData;
    }

    private JComboBox<String> getStudentTypeDropDown() throws SQLException {
        try {
            JComboBox<String> cb = new JComboBox<>();

            studentTypeList = courseMetaData.getStudentTypes();
            for (StudentType studentType : studentTypeList) {
                cb.addItem(studentType.getName());
            }

            cb.setEditable(false);

            return cb;
        } catch(SQLException e) {
            LOG.error("Cannot retrieve student types in the drop down.");
            throw e;
        }
    }

    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        BackButton backButton = new BackButton();

        try {
            studentTypeCB = getStudentTypeDropDown();
        } catch (SQLException e) {
            LOG.error("Failed to get student types: {}", e.getMessage());
        }


        studentFirstNameTextField = new JTextField("First name");
        studentMiddleInitialTextField = new JTextField("Middle name initial");
        studentLastNameTextField = new JTextField("Last name");
        studentEmailTextField = new JTextField("Email");
        studentYearTextField = new JTextField("Year");
        studentSchoolIdTextField = new JTextField("School ID");
        studentMajorTextField = new JTextField("Student's major");

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        GridBagConstraints titleGBC = new GridBagConstraints();
        titleGBC.anchor = GridBagConstraints.WEST;
        titleGBC.gridx = 0;
        titleGBC.gridy = 0;

        GridBagConstraints descriptionGBC = new GridBagConstraints();
        descriptionGBC.gridwidth = GridBagConstraints.REMAINDER;
        descriptionGBC.fill = GridBagConstraints.HORIZONTAL;
        descriptionGBC.gridx = 0;
        descriptionGBC.gridy = 1;

        GridBagConstraints studentFirstNameGBC = new GridBagConstraints();
        studentFirstNameGBC.fill = GridBagConstraints.HORIZONTAL;
        studentFirstNameGBC.gridx = 0;
        studentFirstNameGBC.gridy = 2;

        GridBagConstraints studentMiddleInitialGBC = new GridBagConstraints();
        studentMiddleInitialGBC.fill = GridBagConstraints.HORIZONTAL;
        studentMiddleInitialGBC.gridx = 0;
        studentMiddleInitialGBC.gridy = 3;

        GridBagConstraints studentLastNameGBC = new GridBagConstraints();
        studentLastNameGBC.fill = GridBagConstraints.HORIZONTAL;
        studentLastNameGBC.gridx = 0;
        studentLastNameGBC.gridy = 4;

        GridBagConstraints studentTypeGBC = new GridBagConstraints();
        studentTypeGBC.fill = GridBagConstraints.HORIZONTAL;
        studentTypeGBC.gridx = 0;
        studentTypeGBC.gridy = 5;

        GridBagConstraints studentSchoolIdGBC = new GridBagConstraints();
        studentSchoolIdGBC.fill = GridBagConstraints.HORIZONTAL;
        studentSchoolIdGBC.gridx = 1;
        studentSchoolIdGBC.gridy = 2;

        GridBagConstraints studentYearGBC = new GridBagConstraints();
        studentYearGBC.fill = GridBagConstraints.HORIZONTAL;
        studentYearGBC.gridx = 1;
        studentYearGBC.gridy = 3;

        GridBagConstraints studentMajorGBC = new GridBagConstraints();
        studentMajorGBC.fill = GridBagConstraints.HORIZONTAL;
        studentMajorGBC.gridx = 1;
        studentMajorGBC.gridy = 4;

        GridBagConstraints studentEmailGBC = new GridBagConstraints();
        studentEmailGBC.fill = GridBagConstraints.HORIZONTAL;
        studentEmailGBC.gridx = 1;
        studentEmailGBC.gridy = 5;

        GridBagConstraints submitButtonGBC = new GridBagConstraints();
        submitButtonGBC.anchor = GridBagConstraints.EAST;
        submitButtonGBC.gridx = 1;
        submitButtonGBC.gridy = 6;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.anchor = GridBagConstraints.WEST;
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 6;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(studentFirstNameTextField, studentFirstNameGBC);
        add(studentMiddleInitialTextField, studentMiddleInitialGBC);
        add(studentLastNameTextField, studentLastNameGBC);
        add(studentSchoolIdTextField, studentSchoolIdGBC);
        add(studentYearTextField, studentYearGBC);
        add(studentEmailTextField, studentEmailGBC);
        add(studentMajorTextField, studentMajorGBC);
        add(studentTypeCB, studentTypeGBC);
        add(submitButton, submitButtonGBC);
        add(backButton, backButtonGBC);

    }

    public Major getMajorFromName(String majorName) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            boolean majorExists = create
                    .fetchExists(
                            selectFrom(MAJOR)
                            .where(MAJOR.NAME.eq(majorName))
                    );

            // create major if it doesn't exist
            if (!majorExists) {
                MajorRecord majorRecord = create.newRecord(MAJOR);
                majorRecord.setName(majorName);
                majorRecord.store();
            }

            return create
                    .selectFrom(MAJOR)
                    .where(MAJOR.NAME.eq(majorName))
                    .fetchOneInto(Major.class);
        }
    }

    // for submit button
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Major major = getMajorFromName(studentMajorTextField.getText());

            StudentMetaData studentMetaData = new StudentMetaData(
                    studentFirstNameTextField.getText(),
                    studentMiddleInitialTextField.getText(),
                    studentLastNameTextField.getText(),
                    studentSchoolIdTextField.getText(),
                    studentEmailTextField.getText(),
                    major,
                    Short.parseShort(studentYearTextField.getText()),
                    getStudentTypeFromName((String) studentTypeCB.getSelectedItem())
            );

            studentMetaData.enrollInCourse(courseMetaData.getCourse());

            GradingApplication.PAGE_LOADER.loadPreviousPage();
        } catch (SQLException e1) {
            LOG.error("Error creating studentMetaData: {}" + e1.getMessage());
        }
    }

    private StudentType getStudentTypeFromName(String selectedStudentType) {
        for (StudentType studentType : studentTypeList) {
            if (studentType.getName().equals(selectedStudentType)) {
                return studentType;
            }
        }

        return null;
    }
}