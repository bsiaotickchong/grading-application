package gui;

import javax.swing.*;
import java.awt.*;

abstract class Page extends JPanel {

    private final String name;
    private final String description;

    public Page(String name,
                String description) {
        super(new GridBagLayout());

        this.name = name;
        this.description = description;

        // always load page upon instantiation
        loadPage();
    }

    public abstract void loadPage();
}
