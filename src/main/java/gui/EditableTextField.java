package gui;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public abstract class EditableTextField extends JTextField implements FocusListener {

    public EditableTextField(String text) {
        super(text);
    }

    @Override
    public void focusGained(FocusEvent e) {
        // do nothing
    }

    @Override
    public void focusLost(FocusEvent e) {
        updateText();
    }

    abstract void updateText();
}
