package gui;

import courses.CourseMetaData;
import courses.CourseMetaDataManager;
import database.MetaData;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CourseList extends JScrollPane {
    public CourseList(JPanel courseListPanel) {
        super(courseListPanel);
        setBorder(BorderFactory.createLineBorder(Color.black));

        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));

        CourseMetaDataManager courseMetaDataManager = new CourseMetaDataManager();

        try {
            List<MetaData> courseMetaDatas = courseMetaDataManager.getAllMetaData();

            for (MetaData courseMetaData : courseMetaDatas) {
                courseListPanel.add(new CourseBox(((CourseMetaData) courseMetaData).getCourse()));
            }
        } catch (Exception e) {
            // TODO: output error to user?
            JTextArea textArea = new JTextArea(5, 20);
            textArea.setEditable(false);
            textArea.append("Error loading CourseList");
            e.printStackTrace();
        }
    }
}
