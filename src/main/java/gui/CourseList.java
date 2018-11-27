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

        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));

        CourseMetaDataManager courseMetaDataManager = new CourseMetaDataManager();

        try {
            List<MetaData> courseMetaDatas = courseMetaDataManager.getAllMetaData();

            JTextArea textArea = new JTextArea(5, 10);
            textArea.setEditable(false);

            for (MetaData courseMetaData : courseMetaDatas) {
                textArea.append(((CourseMetaData) courseMetaData).getCourse().toString());
                textArea.append("\n");
            }

            courseListPanel.add(textArea);
        } catch (Exception e) {
            // TODO: output error to user?
            JTextArea textArea = new JTextArea(5, 20);
            textArea.setEditable(false);
            textArea.append("Error loading CourseList");
            e.printStackTrace();
        }
    }
}
