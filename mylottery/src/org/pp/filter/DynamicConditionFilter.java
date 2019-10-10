package org.pp.filter;

import java.util.LinkedList;
import java.util.List;


public class DynamicConditionFilter extends DefaultConditionFilter {

    private final List<String> condition;

    private List<String> infoList;

    private DynamicConditionFilter(List<String> condition) {
        this.condition = condition;
    }

    public static DynamicConditionFilter getInstance(List<String> condition) {
        return new DynamicConditionFilter(condition);
    }

    public List<String> getCondition() {
        return condition;
    }

    public List<String> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<String> infoList) {
        this.infoList = infoList;
    }
}
