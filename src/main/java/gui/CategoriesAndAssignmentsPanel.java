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
import java.util.ArrayList;
import java.util.List;

public class CategoriesAndAssignmentsPanel extends JPanel implements ItemListener {

    private final static Logger LOG = LoggerFactory.getLogger(CategoriesAndAssignmentsPanel.class);
    private static final int PADDING = 10;
    private static final String UNIQUE_SEPARATOR = "SDF78SGDHOSRGEWwgGLhEGg";

    private CourseMetaData courseMetaData;
    private JPanel assignmentListCards;
    private List<AssignmentList> assignmentListList;
    private JLabel weightTotal;
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
        assignmentListList = new ArrayList<>();
        reloadPanel();
    }

    public void reloadPanel() throws SQLException {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new GridBagLayout());

        JLabel categoryHeader = new JLabel("Assignments");
        categoryHeader.setFont(new Font(categoryHeader.getFont().getName(), Font.BOLD, 16));

        JButton addCategoryButton = new AddCategoryButton();

        categoryCB = getCategoryDropDown();
        studentTypeCB = getStudentTypeForAssignmentsDropDown();

        JPanel comboBoxesPanel = new JPanel();
        comboBoxesPanel.setLayout(new BoxLayout(comboBoxesPanel, BoxLayout.Y_AXIS));
        comboBoxesPanel.add(categoryCB);
        comboBoxesPanel.add(studentTypeCB);

        addAssignmentLists();

        weightTotal = new JLabel();
        updateWeightTotal();

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

        GridBagConstraints weightTotalGBC = new GridBagConstraints();
        weightTotalGBC.weightx = .5;
        weightTotalGBC.weighty = .1;
        weightTotalGBC.gridx = 0;
        weightTotalGBC.gridy = 1;

        GridBagConstraints assignmentListGBC = new GridBagConstraints();
        assignmentListGBC.weighty = 1.0;
        assignmentListGBC.gridwidth = 2;
        assignmentListGBC.fill = GridBagConstraints.BOTH;
        assignmentListGBC.gridx = 0;
        assignmentListGBC.gridy = 2;

        add(categoryHeader, categoryHeaderGBC);
        add(addCategoryButton, addCategoryGBC);

        add(weightTotal, weightTotalGBC);
        add(comboBoxesPanel, comboBoxesPanelGBC);
        add(assignmentListCards, assignmentListGBC);

        redrawPanel();
    }

    public void redrawPanel() {
        invalidate();
        validate();
        repaint();
    }

    private void addAssignmentLists() throws SQLException {
        for (Category category : courseMetaData.getCategories()) {
            for (StudentType studentType : courseMetaData.getEnrolledStudentTypes()) {
                String cardName = category.getName() + UNIQUE_SEPARATOR + studentType.getName();
                AssignmentList assignmentList = new AssignmentList(
                        courseMetaData.getAssignmentMetaDatasForCategory(category),
                        studentType,
                        cardName,
                        this,
                        width);
                assignmentListList.add(assignmentList);
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

    private double getTotalWeight(String assignmentListIdentifier) {
        try {
            for (AssignmentList assignmentList : assignmentListList) {
                if (assignmentListIdentifier.equals(assignmentList.getIdentifier())) {
                    return assignmentList.getTotalWeight();
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to get total weight: {}", e.getMessage());
        }

        return -1;
    }

    public void updateWeightTotal() {
        String assignmentListIdentifier = getAssignmentListIdentifier();

        weightTotal.setOpaque(true);
        double totalWeight = getTotalWeight(assignmentListIdentifier);
        if (totalWeight > 100) {
            weightTotal.setBackground(Color.RED);
            weightTotal.setToolTipText("Weight total is over 100%! Grades are still calculated though.");
        } else if (totalWeight != 100) {
            weightTotal.setBackground(Color.YELLOW);
            weightTotal.setToolTipText("Weight total is below 100%! Grades are still calculated though.");
        } else {
            weightTotal.setBackground(Color.GREEN);
            weightTotal.setToolTipText("Weight total is equal to 100%!");
        }

        weightTotal.setText(String.format("Total Weight: %.1f%%", totalWeight));
    }

    private String getAssignmentListIdentifier() {
        return categoryCB.getSelectedItem() + UNIQUE_SEPARATOR + studentTypeCB.getSelectedItem();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String assignmentListIdentifier = getAssignmentListIdentifier();
        CardLayout cl = (CardLayout) assignmentListCards.getLayout();
        cl.show(assignmentListCards, assignmentListIdentifier);

        updateWeightTotal();

        LOG.debug("Changed to Card: {}", assignmentListIdentifier);
    }
}
