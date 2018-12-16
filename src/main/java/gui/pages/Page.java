package gui.pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class Page extends JPanel implements MouseListener {

    final int titleFontSize = 30;
    private final String title;
    private final String description;

    public Page(String name,
                String description) {
        super(new GridBagLayout());

        this.title = name;
        this.description = description;

        this.addMouseListener(this);
    }

    public void reloadPage() {
        removeAll();
        loadPage();
    }

    public abstract void loadPage();

    public void redrawPage() {
        revalidate();
        repaint();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    /*
        Implementing MouseListener is necessary to grab the focus when somewhere on the page is clicked
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        this.grabFocus();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
