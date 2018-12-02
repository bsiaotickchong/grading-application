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
    private static final String UNIQUE_SEPARATOR = "SDF78SGDHOSRGEWwgGLhEGg";

    private CourseMetaData courseMetaData;
    private JPanel assignmentListCards;
    private final int width;
    private final int height;
    private JComboBox categoryCB;
    private JComboBox studentTypeCB;

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

        categoryCB = getCategoryDropDown();
        studentTypeCB = getStudentTypeForAssignmentsDropDown();

        JPanel comboBoxesPanel = new JPanel();
        comboBoxesPanel.setLayout(new BoxLayout(comboBoxesPanel, BoxLayout.Y_AXIS));
        comboBoxesPanel.add(categoryCB);
        comboBoxesPanel.add(studentTypeCB);

        addAssignmentLists();

        GridBagConstraints categoryHeaderGBC = new GridBagConstraints();
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

        GridBagConstraints comboBoxesPanelGBC = new GridBagConstraints();
        comboBoxesPanelGBC.weightx = .5;
        comboBoxesPanelGBC.weighty = .1;
        comboBoxesPanelGBC.gridx = 1;
        comboBoxesPanelGBC.gridy = 1;

        GridBagConstraints assignmentListGBC = new GridBagConstraints();
        assignmentListGBC.weighty = 1.0;
        assignmentListGBC.gridwidth = 2;
        assignmentListGBC.fill = GridBagConstraints.BOTH;
        assignmentListGBC.gridx = 0;
        assignmentListGBC.gridy = 2;

        add(categoryHeader, categoryHeaderGBC);
        add(addCategoryButton, addCategoryGBC);

        add(comboBoxesPanel, comboBoxesPanelGBC);
        add(assignmentListCards, assignmentListGBC);

        invalidate();
        validate();
        repaint();
    }

    private void addAssignmentLists() throws SQLException {
        for (Category category : courseMetaData.getCategories()) {
            for (StudentType studentType : courseMetaData.getEnrolledStudentTypes()) {
                JScrollPane assignmentList = new AssignmentList(
                        courseMetaData.getAssignmentMetaDatasForCategory(category),
                        studentType,
                        width);
                String cardName = category.getName() + UNIQUE_SEPARATOR + studentType.getName();
                assignmentListCards.add(assignmentList, cardName);
            }
        }
    }

    private JComboBox getCategoryDropDown() throws SQLException {
        JComboBox categoryCB = new JComboBox(courseMetaData.getCategoriesAsStrings());

        categoryCB.setEditable(false);
        categoryCB.addItemListener(this);

        return categoryCB;
    }

    private JComboBox getStudentTypeForAssignmentsDropDown() throws SQLException {
        JComboBox cb = new JComboBox(courseMetaData.getStudentTypesAsStrings());

        cb.setEditable(false);
        cb.addItemListener(this);

        return cb;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String combinedSelection = categoryCB.getSelectedItem() + UNIQUE_SEPARATOR + studentTypeCB.getSelectedItem();

        CardLayout cl = (CardLayout) assignmentListCards.getLayout();
        cl.show(assignmentListCards, combinedSelection);

        LOG.info("Changed to Card: {}", combinedSelection);
    }
}
