package gui;

import courses.CourseMetaData;
import org.jooq.grading_app.db.h2.tables.pojos.Category;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

public class CategoriesAndAssignmentsPanel extends JPanel implements ItemListener {

    private final static Logger LOG = LoggerFactory.getLogger(CategoriesAndAssignmentsPanel.class);
    private static final int PADDING = 10;

    private CourseMetaData courseMetaData;
    private JPanel assignmentListCards;
    private final int width;
    private final int height;

    public CategoriesAndAssignmentsPanel(CourseMetaData courseMetaData,
                                         int width,
                                         int height) throws SQLException {
        this.courseMetaData = courseMetaData;
        this.width = width;
        this.height = height;

        assignmentListCards = new JPanel(new CardLayout());
        reloadPanel();
    }

    public void reloadPanel() throws SQLException {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new GridBagLayout());

        JLabel categoryHeader = new JLabel("Categories");
        categoryHeader.setFont(new Font(categoryHeader.getFont().getName(), Font.BOLD, 20));

        JButton addCategoryButton = new AddCategoryButton();

        JComboBox categoryCB = getCategoryDropDown();

        for (Category category : courseMetaData.getCategories()) {
            for (StudentType studentType : courseMetaData.getEnrolledStudentTypes()) {
                JScrollPane assignmentList = new AssignmentList(
                        courseMetaData.getAssignmentMetaDatasForCategory(category),
                        studentType,
                        width);
                assignmentListCards.add(assignmentList);
            }
        }

        GridBagConstraints categoryHeaderGBC = new GridBagConstraints();
        categoryHeaderGBC.anchor = GridBagConstraints.NORTHWEST;
        categoryHeaderGBC.weightx = .5;
        categoryHeaderGBC.weighty = .1;
        categoryHeaderGBC.gridx = 0;
        categoryHeaderGBC.gridy = 0;
        categoryHeaderGBC.ipadx = PADDING;
        categoryHeaderGBC.ipady = PADDING;

        GridBagConstraints addCategoryGBC = new GridBagConstraints();
        addCategoryGBC.weightx = .5;
        addCategoryGBC.weighty = .1;
        addCategoryGBC.gridx = 1;
        addCategoryGBC.gridy = 0;

        GridBagConstraints categoryCBGBC = new GridBagConstraints();
        categoryCBGBC.weightx = .5;
        categoryCBGBC.weighty = .1;
        categoryCBGBC.gridx = 1;
        categoryCBGBC.gridy = 1;

        GridBagConstraints assignmentListGBC = new GridBagConstraints();
        assignmentListGBC.weighty = 1.0;
        assignmentListGBC.gridwidth = 2;
        assignmentListGBC.fill = GridBagConstraints.BOTH;
        assignmentListGBC.gridx = 0;
        assignmentListGBC.gridy = 2;

        add(categoryHeader, categoryHeaderGBC);
        add(addCategoryButton, addCategoryGBC);

        add(categoryCB, categoryCBGBC);
        add(assignmentListCards, assignmentListGBC);

        invalidate();
        validate();
        repaint();
    }

    private JComboBox getCategoryDropDown() throws SQLException {
        JComboBox categoryCB = new JComboBox(courseMetaData.getCategories().stream()
                .map(Category::getName).toArray());

        categoryCB.setEditable(false);
        categoryCB.addItemListener(this);

        return categoryCB;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        CardLayout cl = (CardLayout) assignmentListCards.getLayout();
        cl.show(assignmentListCards, (String) e.getItem());

        LOG.info("Changed to Card: {}", e.getItem());
    }
}
