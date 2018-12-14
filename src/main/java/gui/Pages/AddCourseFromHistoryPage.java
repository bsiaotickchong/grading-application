package gui.Pages;

import application.GradingApplication;
import assignments.AssignmentMetaData;
import courses.CourseMetaData;
import courses.CourseMetaDataManager;
import database.H2DatabaseUtil;
import database.MetaData;
import gui.BackButton;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.Assignment;
import org.jooq.grading_app.db.h2.tables.pojos.Category;
import org.jooq.grading_app.db.h2.tables.pojos.TimeOfYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.jooq.grading_app.db.h2.tables.TimeOfYear.TIME_OF_YEAR;

public class AddCourseFromHistoryPage extends Page implements ActionListener, ItemListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddCourseFromHistoryPage.class);

    private JButton submitButton;
    private JComboBox courseCB;
    private JComboBox timeOfYearCB;

    public AddCourseFromHistoryPage() {
        super("Add course from historical data", "Select a course to copy all the categories and assignments");
    }

    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        try {
            courseCB = getCourseDropDown();
        } catch (SQLException e) {
            LOG.error("Could not create course drop down: {}", e.getMessage());
        }

        timeOfYearCB = getTimeOfYearDropDown();

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        JButton backButton = new BackButton();


        GridBagConstraints titleGBC = new GridBagConstraints();
        titleGBC.anchor = GridBagConstraints.WEST;
        titleGBC.gridx = 0;
        titleGBC.gridy = 0;

        GridBagConstraints descriptionGBC = new GridBagConstraints();
        descriptionGBC.gridwidth = GridBagConstraints.REMAINDER;
        descriptionGBC.fill = GridBagConstraints.HORIZONTAL;
        descriptionGBC.gridx = 0;
        descriptionGBC.gridy = 1;

        GridBagConstraints courseGBC = new GridBagConstraints();
        courseGBC.gridx = 0;
        courseGBC.gridy = 2;

        GridBagConstraints timeOfYearGBC = new GridBagConstraints();
        timeOfYearGBC.gridx = 1;
        timeOfYearGBC.gridy = 2;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.anchor = GridBagConstraints.WEST;
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;

        GridBagConstraints submitButtonGBC = new GridBagConstraints();
        submitButtonGBC.anchor = GridBagConstraints.EAST;
        submitButtonGBC.gridx = 1;
        submitButtonGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(courseCB, courseGBC);
        add(timeOfYearCB, timeOfYearGBC);
        add(backButton, backButtonGBC);
        add(submitButton, submitButtonGBC);
    }

    private JComboBox getTimeOfYearDropDown() {
        JComboBox timeOfYearCB = new JComboBox();

        for (TimeOfYear timeOfYear : getTimeOfYears()) {
            timeOfYearCB.addItem(timeOfYear);
        }

        timeOfYearCB.setEditable(false);
        timeOfYearCB.addItemListener(this);

        return timeOfYearCB;
    }

    private List<TimeOfYear> getTimeOfYears() {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .selectFrom(TIME_OF_YEAR)
                    .fetchInto(TimeOfYear.class);
        } catch (SQLException e) {
            LOG.error("Could not get time of years: {}", e.getMessage());
        }

        return null;
    }

    private TimeOfYear getSelectedTimeOfYear() {
        return (TimeOfYear) timeOfYearCB.getSelectedItem();
    }

    private JComboBox getCourseDropDown() throws SQLException {
        CourseMetaDataManager courseMetaDataManager = new CourseMetaDataManager();
        JComboBox courseCB = new JComboBox();

        for (MetaData metaData : courseMetaDataManager.getAllMetaData()) {
            CourseMetaData courseMetaData = (CourseMetaData) metaData;
            String courseString = getCourseString(courseMetaData);
            courseCB.addItem(courseString);
        }

        courseCB.setEditable(false);
        courseCB.addItemListener(this);

        return courseCB;
    }

    private String getCourseString(CourseMetaData courseMetaData) {
        return String.format("%s | %s, %s %s",
                courseMetaData.getName(),
                courseMetaData.getDescription(),
                courseMetaData.getTimeOfYear().getSemester().getLiteral(),
                courseMetaData.getTimeOfYear().getYear().toString()
                );
    }

    // for submit button
    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info("Submit button clicked");

        if (e.getSource() == submitButton) {
            try {
                CourseMetaData selectedCourse = getSelectedCourse();
                CourseMetaData courseMetaData = copyCourseMetaData(selectedCourse, getSelectedTimeOfYear());
            } catch (SQLException s) {
                LOG.error("Could not create course: {}", s.getMessage());
            }

            GradingApplication.PAGE_LOADER.loadPreviousPage();
        }
    }

    // copy name, description, and all categories and assignments (not weights)
    // TODO: copy over max grades per assignment as well
    private CourseMetaData copyCourseMetaData(CourseMetaData courseMetaData,
                                              TimeOfYear timeOfYear) throws SQLException {
        CourseMetaData newCourseMetaData = new CourseMetaData(
                courseMetaData.getName(),
                timeOfYear,
                courseMetaData.getDescription()
        );

        // add all categories and assignments
        List<Category> categories = courseMetaData.getCategories();
        for (Category category : categories) {
            Category copiedCategory = newCourseMetaData.addCategory(category.getName());

            for (AssignmentMetaData assignmentMetaData : courseMetaData.getAssignmentMetaDatasForCategory(category)) {
                newCourseMetaData.addAssignment(
                        copiedCategory,
                        assignmentMetaData.getExtraCredit(),
                        assignmentMetaData.getName());
            }
        }

        return newCourseMetaData;
    }

    private CourseMetaData getSelectedCourse() throws SQLException {
        CourseMetaDataManager courseMetaDataManager = new CourseMetaDataManager();

        for (MetaData metaData : courseMetaDataManager.getAllMetaData()) {
            CourseMetaData courseMetaData = (CourseMetaData) metaData;
            String courseString = getCourseString(courseMetaData);

            if (courseCB.getSelectedItem().equals(courseString)) {
                return courseMetaData;
            }
        }

        return null;
    }

    // for course selection
    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
