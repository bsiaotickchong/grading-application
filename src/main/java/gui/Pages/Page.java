package gui.Pages;

import javax.swing.*;
import java.awt.*;

public abstract class Page extends JPanel {

    final int titleFontSize = 30;
    private final String title;
    private final String description;

    public Page(String name,
                String description) {
        super(new GridBagLayout());

        this.title = name;
        this.description = description;
    }

    public abstract void loadPage();

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
