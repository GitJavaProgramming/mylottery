package org.pp.component.event;

import java.awt.*;

public class SelectEvent extends AWTEvent {

    public SelectEvent(Object source, int id) {
        this(source, null, id);
    }

    public SelectEvent(Object source, Object lastSelection, int id) {
        this(source, lastSelection, id, 0);
    }

    public SelectEvent(Object source, Object lastSelection, int id, int clickCount) {
        super(source, id);
        this.setLastSelection(lastSelection);
        this.clickCount = clickCount;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public Object getLastSelection() {
        return lastSelection;
    }

    public void setLastSelection(Object lastSelection) {
        this.lastSelection = lastSelection;
    }

    private int clickCount;
    private Object lastSelection;
}