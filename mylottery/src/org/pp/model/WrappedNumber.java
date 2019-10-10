package org.pp.model;

import org.pp.util.NumberUtil;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-16
 * Time: 下午6:47
 * To change this template use File | Settings | File Templates.
 */
public class WrappedNumber {

    private int num; // 号码
    private boolean isBig; // 大小
    private boolean isOdd; // 奇偶
    private boolean isPrime; // 质数合数
    private RemainderByThreeEnum remainderByThree; // 除3余数
    private FiveElementEnum element; // 五行元素类型

    private boolean bigTail;

    private WrappedNumber(int num) {
        this.num = num;
        this.isBig = NumberUtil.isBig(num);
        this.isOdd = NumberUtil.isOdd(num);
        this.isPrime = NumberUtil.isPrime(num);
        this.remainderByThree = NumberUtil.getRemainderByThreeEnum(num);
        element = FiveElementEnum.elemType(num);
        bigTail = NumberUtil.bigTail(num);
    }

    public FiveElementEnum getElement() {
        return element;
    }

    public boolean isBig() {
        return isBig;
    }

    public boolean isOdd() {
        return isOdd;
    }

    public boolean isPrime() {
        return isPrime;
    }

    public int getNum() {
        return num;
    }

    public RemainderByThreeEnum getRemainderByThree() {
        return remainderByThree;
    }

    public static WrappedNumber valueOf(int num) {
        if (num <= 0 || num > 35) {
            throw new IllegalArgumentException("大乐透前区号码必须在[1,35]之间。");
        }
        return new WrappedNumber(num);
    }
}
