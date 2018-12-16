package gui.assignments;

import assignments.AssignmentMetaData;
import gui.courses.CategoriesAndAssignmentsPanel;
import gui.pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.List;

public class GradesAndStudentsPanel extends JPanel implements ItemListener {

    private final static Logger LOG = LoggerFactory.getLogger(CategoriesAndAssignmentsPanel.class);
    private static final int PADDING = 10;
    private static final String UNIQUE_SEPARATOR = "SDF78SGDHOSRGEWwgGLhEGg";

    private Page parentPage;
    private AssignmentMetaData assignmentMetaData;
    private JPanel studentListCards;
    private JLabel maxPoints;
    private final int width;
    private final int height;
    private JComboBox studentTypeCB;

    public GradesAndStudentsPanel(AssignmentMetaData assignmentMetaData,
                                  Page parentPage,
                                  int width,
                                  int height) throws SQLException {
        this.assignmentMetaData = assignmentMetaData;
        this.parentPage = parentPage;
        this.width = width;
        this.height = height;

        studentListCards = new JPanel(new CardLayout());
        reloadPanel();
    }

    public void reloadPanel() throws SQLException {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new GridBagLayout());

        JLabel header = new JLabel("Grades per student");
        header.setFont(new Font(header.getFont().getName(), Font.BOLD, 16));

        studentTypeCB = getStudentTypeDropDown();

        JPanel comboBoxesPanel = new JPanel();
        comboBoxesPanel.setLayout(new BoxLayout(comboBoxesPanel, BoxLayout.Y_AXIS));
        comboBoxesPanel.add(studentTypeCB);

        addAssignmentStudentList();

        maxPoints = new JLabel();

        GridBagConstraints headerGBC = new GridBagConstraints();
        headerGBC.anchor = GridBagConstraints.CENTER;
        headerGBC.fill = GridBagConstraints.HORIZONTAL;
        headerGBC.weightx = .5;
        headerGBC.weighty = .1;
        headerGBC.gridx = 0;
        headerGBC.gridy = 1;

        GridBagConstraints comboBoxesPanelGBC = new GridBagConstraints();
        comboBoxesPanelGBC.anchor = GridBagConstraints.EAST;
        comboBoxesPanelGBC.weightx = .5;
        comboBoxesPanelGBC.weighty = .1;
        comboBoxesPanelGBC.gridx = 1;
        comboBoxesPanelGBC.gridy = 1;

        GridBagConstraints assignmentStudentListGBC = new GridBagConstraints();
        assignmentStudentListGBC.weighty = 1.0;
        assignmentStudentListGBC.gridwidth = 2;
        assignmentStudentListGBC.fill = GridBagConstraints.BOTH;
        assignmentStudentListGBC.gridx = 0;
        assignmentStudentListGBC.gridy = 2;

        add(header, headerGBC);
        add(comboBoxesPanel, comboBoxesPanelGBC);
        add(studentListCards, assignmentStudentListGBC);
    }

    private void addAssignmentStudentList() throws SQLException {
        AssignmentStudentList assignmentAllStudentList = new AssignmentStudentList(
                null,
                parentPage,
                assignmentMetaData.getCourseMetaData(),
                assignmentMetaData,
                width
        );
        studentListCards.add(assignmentAllStudentList, "All");

        List<StudentType> enrolledStudentTypes = assignmentMetaData.getCourseMetaData().getEnrolledStudentTypes();
        for(StudentType studentType : enrolledStudentTypes){
            String cardName = studentType.getName();
            AssignmentStudentList assignmentStudentList = new AssignmentStudentList(
                    studentType,
                    parentPage,
                    assignmentMetaData.getCourseMetaData(),
                    assignmentMetaData,
                    width
            );
            studentListCards.add(assignmentStudentList, cardName);
        }
    }

    private JComboBox getStudentTypeDropDown() throws SQLException{
        JComboBox cb = new JComboBox();

        cb.addItem("All");

        for (Object studentType : assignmentMetaData.getCourseMetaData().getUniqueEnrolledStudentTypesAsStrings()){
            String studentTypeString = (String) studentType;
            cb.addItem(studentTypeString);
        }

        cb.setEditable(false);
        cb.addItemListener(this);

        return cb;
    }

    private double getMaxPoint(){
        return 5;
    }

    private String getStudentListIdentifier() {
        return (String) studentTypeCB.getSelectedItem();
//        if(sel.equals("All")) {
//            return null;
//        } else {
//            return sel;
//        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String assignmentStudentListIdentifier = getStudentListIdentifier();
        CardLayout cl = (CardLayout) studentListCards.getLayout();
        cl.show(studentListCards, assignmentStudentListIdentifier);

        LOG.debug("Changed to Card: {}", assignmentStudentListIdentifier);
    }


}
