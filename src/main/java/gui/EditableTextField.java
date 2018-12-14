package gui;

import gui.Pages.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;

public abstract class EditableTextField extends JTextField implements FocusListener {

    private final static Logger LOG = LoggerFactory.getLogger(EditableTextField.class);

    private final Page parentPage;

    public EditableTextField(String text, Page parentPage) {
        super(text);
        addFocusListener(this);
        this.parentPage = parentPage;
    }

    @Override
    public void focusGained(FocusEvent e) {
        // do nothing
    }

    @Override
    public void focusLost(FocusEvent e) {
        try {
            updateText(this.getText());
        } catch (SQLException s) {
            LOG.error(s.getMessage());
            s.printStackTrace();
        }
    }

    abstract void updateText(String updatedText) throws SQLException;

    public Page getParentPage() {
        return parentPage;
    }
}
