package org.pp.model;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-16
 * Time: 下午6:52
 * To change this template use File | Settings | File Templates.
 */
public enum FiveElementEnum {

    JIN("金"), MU("木"), SHUI("水"), HUO("火"), TU("土");

    /* 金(收敛) */
    private static final int[] kingArray = new int[]{9, 10, 21, 22, 33, 34};
    /* 木（伸展） */
    private static final int[] muArray = new int[]{3, 4, 15, 16, 27, 28};
    /* 水（润下） */
    private static final int[] shuiArray = new int[]{1, 12, 13, 24, 25};
    /* 火（炎上） */
    private static final int[] huoArray = new int[]{6, 7, 18, 19, 30, 31};
    /* 土（中和） */
    private static final int[] tuArray = new int[]{2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35};

    static {
        JIN.setValues(kingArray);
        MU.setValues(muArray);
        SHUI.setValues(shuiArray);
        HUO.setValues(huoArray);
        TU.setValues(tuArray);
    }

    private final String elemName;

    private int[] values;

    private FiveElementEnum(String elemName) {
        this.elemName = elemName;
    }

    public String getElemName() {
        return elemName;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    /**
     * 五行元素值
     *
     * @param num
     * @return 五行元素值
     */
    public static FiveElementEnum elemType(int num) {
        if (Arrays.binarySearch(tuArray, num) >= 0) {
            return FiveElementEnum.TU;
        } else if (Arrays.binarySearch(kingArray, num) >= 0) {
            return FiveElementEnum.JIN;
        } else if (Arrays.binarySearch(muArray, num) >= 0) {
            return FiveElementEnum.MU;
        } else if (Arrays.binarySearch(shuiArray, num) >= 0) {
            return FiveElementEnum.SHUI;
        } else {
            return FiveElementEnum.HUO;
        }
    }
}
