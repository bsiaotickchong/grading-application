package gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCategoryButton extends JButton implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddCategoryButton.class);

    public AddCategoryButton() {
        super("Add Category");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info("Clicked AddCategory button");
    }
}
