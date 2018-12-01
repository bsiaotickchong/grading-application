package gui;

import courses.CourseMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class PageLoader {

    private final static Logger LOG = LoggerFactory.getLogger(PageLoader.class);

    private final String applicationName;
    private final PageHistory pageHistory;
    private final JFrame frame;

    public PageLoader(String applicationName) {
        this.applicationName = applicationName;
        pageHistory = new PageHistory();
        frame = new JFrame(applicationName);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Must be instantiated with a page to display
    public void instantiate(Page startingPage) {
        loadNewPage(startingPage);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setVisible(true);
    }

    public void loadNewPage(Page newPage) {
        if (pageHistory.hasCurrentPage()) {
            pageHistory.addPreviousPage(pageHistory.getCurrentPage());
            frame.remove(pageHistory.getCurrentPage());
        }

        loadPage(newPage);
    }

    public void loadPreviousPage() {
        if (!pageHistory.hasCurrentPage()) {
            LOG.error("No current page");
            return;
        } else if (!pageHistory.hasPreviousPage()) {
            LOG.error("No previous page");
            return;
        }

        frame.remove(pageHistory.getCurrentPage());
        pageHistory.setCurrentPage(pageHistory.getPreviousPage());

        loadPage(pageHistory.getCurrentPage());
    }

    private void loadPage(Page page) {
        frame.add(page);
        pageHistory.setCurrentPage(page);
        page.loadPage();

        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
}
