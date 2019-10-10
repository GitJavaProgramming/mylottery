package org.pp.util;

import java.io.UnsupportedEncodingException;
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
        try {
            return new String(config.getString("testNum.name").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFilterNumberPath() {
        try {
            return new String(config.getString("filter.number").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFilterFinalResult() {
        try {
            return new String(config.getString("filter.finalResult").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFixCondition() {
        try {
            return new String(config.getString("filter.FixCondition").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDynamicCondition() {
        try {
            return new String(config.getString("filter.dynamicCondition").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFilterMiddleResult() {
        try {
            return new String(config.getString("filter.middleResult").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLoadNumber() {
        try {
            return new String(config.getString("load.number").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDefaultBuildCondition() {
        try {
            return new String(config.getString("filter.first").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionCurr() {
        try {
            return new String(config.getString("version.curr").getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
