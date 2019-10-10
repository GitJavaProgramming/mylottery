package org.pp.filter;

import base.GLog;
import org.pp.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

public final class FixedConditionFilter extends DefaultConditionFilter {

    private List<String> condition = new ArrayList<String>(100);

    private List<String> infoList;

    public FixedConditionFilter() {
        GLog.logger.info("build fixed condition...");
        initCondition(ConfigUtil.getFixCondition(), condition);
        GLog.logger.info("build fixed condition done...");
    }

    @Override
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
