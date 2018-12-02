package gui;

import gui.Pages.Page;

import java.util.Stack;

public class PageHistory {
    private static Stack<Page> pageStack;
    private static Page currentPage;

    public PageHistory() {
        pageStack = new Stack<>();
    }

    public void addPreviousPage(Page previousPage) {
        pageStack.push(previousPage);
    }

    public Page getPreviousPage() {
        return pageStack.pop();
    }

    public void setCurrentPage(Page currentPage) {
        PageHistory.currentPage = currentPage;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public boolean hasCurrentPage() {
        return currentPage != null;
    }

    public boolean hasPreviousPage() {
        return !pageStack.empty();
    }
}
