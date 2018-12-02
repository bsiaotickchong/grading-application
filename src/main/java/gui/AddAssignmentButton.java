package gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAssignmentButton extends JButton implements ActionListener {

    private final static Logger LOG = LoggerFactory.getLogger(AddAssignmentButton.class);

    public AddAssignmentButton() {
        super("Add Assignment");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info("Clicked AddAssignmentButton");
    }
}
