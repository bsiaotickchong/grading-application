package gui.buttons;

import application.GradingApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackButton extends JButton implements ActionListener {
    private final static Logger LOG = LoggerFactory.getLogger(BackButton.class);

    public BackButton() {
        super("Back");
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info("Back pressed");
        GradingApplication.PAGE_LOADER.loadPreviousPage();
    }
}
