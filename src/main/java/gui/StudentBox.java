package gui;

import gui.Pages.Page;
import students.StudentMetaData;

import javax.swing.*;
import java.awt.*;

public class StudentBox extends JButton {

    private final StudentMetaData studentMetaData;

    public StudentBox(StudentMetaData studentMetaData,
                      Page parentPage,
                      int width,
                      int height) {
        super();
        this.studentMetaData = studentMetaData;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));

        JLabel nameLabel = new JLabel(studentMetaData.getFullName());

        JLabel schoolIdLabel = new JLabel(studentMetaData.getSchoolId());

        JLabel emailLabel = new JLabel(studentMetaData.getEmail());

        GridBagConstraints nameGBC = new GridBagConstraints();
        nameGBC.anchor = GridBagConstraints.WEST;
        nameGBC.weightx = 1.0;
        nameGBC.gridx = 0;

        GridBagConstraints schoolIdGBC = new GridBagConstraints();
        schoolIdGBC.anchor = GridBagConstraints.CENTER;
        schoolIdGBC.weightx = 1.0;
        schoolIdGBC.gridx = 1;

        GridBagConstraints emailGBC = new GridBagConstraints();
        emailGBC.anchor = GridBagConstraints.EAST;
        emailGBC.weightx = 1.0;
        emailGBC.gridx = 2;

        add(nameLabel, nameGBC);
        add(schoolIdLabel, schoolIdGBC);
        add(emailLabel, emailGBC);
    }

    public StudentMetaData getStudentMetaData() {
        return studentMetaData;
    }
}
