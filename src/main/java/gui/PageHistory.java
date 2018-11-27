package gui;

import java.util.Stack;

public class PageHistory {
    private Stack<Page> pageStack;

    public PageHistory() {
        pageStack = new Stack<>();
    }

    public void addPreviousPage(Page previousPage) {
        pageStack.push(previousPage);
    }

    public Page getPreviousPage() {
        return pageStack.pop();
    }
}
