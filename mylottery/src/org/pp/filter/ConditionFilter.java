package org.pp.filter;

import java.util.List;

public interface ConditionFilter {

    public boolean filter(int[] array, boolean isTest);

    public List<String> getCondition();

    public List<String> getInfoList();

    public void setInfoList(List<String> infoList);
}
