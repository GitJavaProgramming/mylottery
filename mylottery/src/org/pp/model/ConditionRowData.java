package org.pp.model;

public class ConditionRowData {
    private String selected;
    private String limit;

    public ConditionRowData() {
    }

    public ConditionRowData(String selected, String limit) {
        this.selected = selected;
        this.limit = limit;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return selected + "-" + limit;
    }
}
