package gui.Pages;

import application.GradingApplication;
import courses.CourseMetaData;
import database.H2DatabaseUtil;
import gui.BackButton;
import org.jooq.DSLContext;
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

public class AddCoursePage extends Page implements ActionListener, ItemListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddCoursePage.class);

    private JTextField courseNameTextField;
    private JButton submitButton;
    private JTextField courseDescTextField;
    private JComboBox timeOfYearCB;

    public AddCoursePage() {
        super("Add Course", "Create a new course");
    }

    @Override
    public void loadPage() {
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        courseNameTextField = new JTextField("Enter course name here...");

        timeOfYearCB = getTimeOfYearDropDown();

        courseDescTextField = new JTextField("Enter course description here...");

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

        GridBagConstraints courseNameGBC = new GridBagConstraints();
        courseNameGBC.gridx = 0;
        courseNameGBC.gridy = 2;
        courseNameGBC.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints timeOfYearGBC = new GridBagConstraints();
        timeOfYearGBC.gridx = 1;
        timeOfYearGBC.gridy = 2;

        GridBagConstraints courseDescGBC = new GridBagConstraints();
        courseDescGBC.gridwidth = 2;
        courseDescGBC.gridx = 0;
        courseDescGBC.gridy = 3;
        courseDescGBC.fill = GridBagConstraints.HORIZONTAL;

        GridBagConstraints submitButtonGBC = new GridBagConstraints();
        submitButtonGBC.anchor = GridBagConstraints.EAST;
        submitButtonGBC.gridx = 1;
        submitButtonGBC.gridy = 4;

        GridBagConstraints backButtonGBC = new GridBagConstraints();
        backButtonGBC.anchor = GridBagConstraints.WEST;
        backButtonGBC.gridx = 0;
        backButtonGBC.gridy = 4;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(courseNameTextField, courseNameGBC);
        add(timeOfYearCB, timeOfYearGBC);
        add(courseDescTextField, courseDescGBC);
        add(submitButton, submitButtonGBC);
        add(backButton, backButtonGBC);
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

    // for submit button
    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info("Submit button clicked");

        if (e.getSource() == submitButton) {
            try {
                TimeOfYear selectedTimeOfYear = getSelectedTimeOfYear();
                CourseMetaData courseMetaData = new CourseMetaData(
                        courseNameTextField.getText(),
                        selectedTimeOfYear,
                        courseDescTextField.getText());
            } catch (SQLException s) {
                LOG.error("Could not create course: {}", s.getMessage());
            }

            GradingApplication.PAGE_LOADER.loadPreviousPage();
        }
    }

    private TimeOfYear getSelectedTimeOfYear() {
        return (TimeOfYear) timeOfYearCB.getSelectedItem();
    }

    // for time of year combo box
    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
