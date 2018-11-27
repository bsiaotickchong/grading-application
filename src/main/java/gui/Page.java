package gui;

import javax.swing.*;

abstract class Page {


    private final String name;
    private final String description;
    private JPanel contentPane;

    public Page(String name,
                String description) {
        this.name = name;
        this.description = description;

        // always load page upon instantiation
        loadPage();
    }

    public abstract void loadPage();

    public JPanel getContentPane() {
        return contentPane;
    }
}
