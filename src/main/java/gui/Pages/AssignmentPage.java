package gui.Pages;

import assignments.AssignmentMetaData;
import gui.AssignmentStudentList;
import gui.Pages.Page;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;

import javax.swing.*;
import java.awt.*;

public class AssignmentPage extends Page {

    StudentType studentType;
    AssignmentMetaData assignmentMetaData;

    public AssignmentPage(AssignmentMetaData A, StudentType studentType) {
        super("Assignment:", A.name);
        this.studentType = studentType;
        this.assignmentMetaData = A;
    }

    @Override
    public void loadPage(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JLabel title = new JLabel(getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, titleFontSize));

        JLabel description = new JLabel(getDescription());

        //before implemented dropdown menu, the name of type will just be hard-coded here:
        String sType = "Undergraduate";

        JLabel type = new JLabel("Stundet Type: " + sType);
        type.setFont(new Font(type.getFont().getName(), Font.ITALIC, 20));

        AssignmentStudentList assignmentStudentList = new AssignmentStudentList(new JPanel(), studentType, this, assignmentMetaData);
        assignmentStudentList.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/2));


        GridBagConstraints titleGBC = new GridBagConstraints();
        titleGBC.anchor = GridBagConstraints.WEST;
        titleGBC.gridx = 0;
        titleGBC.gridy = 0;

        GridBagConstraints descriptionGBC = new GridBagConstraints();
        descriptionGBC.gridwidth = GridBagConstraints.REMAINDER;
        descriptionGBC.fill = GridBagConstraints.HORIZONTAL;
        descriptionGBC.gridx = 0;
        descriptionGBC.gridy = 1;

        GridBagConstraints typeGBC = new GridBagConstraints();
        typeGBC.anchor =GridBagConstraints.WEST;
        typeGBC.gridx = 0;
        typeGBC.gridy = 2;

        add(title, titleGBC);
        add(description, descriptionGBC);
        add(type, typeGBC);
    }
}
