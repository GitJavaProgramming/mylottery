package org.pp.util;

import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-20
 * Time: 下午9:59
 * To change this template use File | Settings | File Templates.
 */
public final class ConfigUtil {
    private static final ResourceBundle config = ResourceBundle.getBundle("config");

    public static boolean isTest() {
        String isTest = config.getString("filter.isTest");
        return new Boolean(isTest).booleanValue();
    }

    public static String getTestPath() {
        return config.getString("testNum.name");
    }

    public static String getFilterNumberPath() {
        return config.getString("filter.number");
    }

    public static String getFilterFinalResult() {
        return config.getString("filter.finalResult");
    }

    public static String getFixCondition() {
        return config.getString("filter.FixCondition");
    }

    public static String getDynamicCondition() {
        return config.getString("filter.dynamicCondition");
    }

    public static String getFilterMiddleResult() {
        return config.getString("filter.middleResult");
    }

    public static String getLoadNumber() {
        return config.getString("load.number");
    }

    public static String getDefaultBuildCondition() {
        return config.getString("filter.first");
    }

    public static String getVersionCurr() {
        return config.getString("version.curr");
    }

}
