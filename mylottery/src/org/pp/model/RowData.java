package org.pp.model;

import org.pp.util.NumberUtil;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-16
 * Time: 下午7:59
 * To change this template use File | Settings | File Templates.
 */
public class RowData {

    private int[] originFrontNumber; // 原始号码
    private int numLength;

    private Map<Integer, Integer> areaRatioByThreeMap;
    private Map<Integer, Integer> remainderByThreeMap;

    public RowData(int[] originData/*元数据数组长度为5*/) {
        this.originFrontNumber = originData;
        this.numLength = originData.length;

        format();

        initAreaRatioByThreeMap();
        initRemainderByThreeMap();
    }

    /**
     * 当前期的ac值
     *
     * @return ac值
     */
    public int ac() {
        return NumberUtil.ac(originFrontNumber);
    }

    /**
     * 当前期的总和
     *
     * @return 和值
     */
    public int sum() {
        return NumberUtil.sum(originFrontNumber);
    }

    /**
     * 本期跨度
     *
     * @return 跨度
     */
    public int kuaDu() {
        return originFrontNumber[4] - originFrontNumber[0];
    }

    /**
     * 跨度点位
     *
     * @return 返回所在七分区号码
     */
    public int[] kuaDuDianWei() {
        return NumberUtil.kuaDuDianWei(kuaDu());
    }

    /**
     * 大号数
     *
     * @return
     */
    public int getBigNumberCount() {
        int count = 0;
        for (int i = 0; i < numLength; i++) {
            if (NumberUtil.isBig(originFrontNumber[i])) {
                count++;
            }
        }
        return count;
    }

    public String getBigNumRatio() {
        int bigNumber = getBigNumberCount();
        return bigNumber + ":" + (5 - bigNumber);
    }

    /**
     * 获取偶数个数
     *
     * @return
     */
    public int getOddCount() {
        int count = 0;
        for (int i = 0; i < numLength; i++) {
            if (NumberUtil.isOdd(originFrontNumber[i])) {
                count++;
            }
        }
        return count;
    }

    public String getOddNumRatio() {
        int oddCount = getOddCount();
        return oddCount + ":" + (5 - oddCount);
    }

    /**
     * 获取素数的个数
     *
     * @return
     */
    public int getPrimerCount() {
        int count = 0;
        for (int i = 0; i < numLength; i++) {
            if (NumberUtil.isPrime(originFrontNumber[i])) {
                count++;
            }
        }
        return count;
    }

    public String getPrimerRatio() {
        int primerCount = getPrimerCount();
        return primerCount + ":" + (5 - primerCount);
    }

    /**
     * 除三余数比
     *
     * @return
     */
    public String getRemainderByThreeCount() {
        return NumberUtil.getRemainderByThreeCount(originFrontNumber);
    }

    /**
     * 本期三区比
     *
     * @return 三区比字符串
     */
    public String areaRatioByThree() {
        return NumberUtil.areaRatioByThree(originFrontNumber);
    }

    /**
     * 三区比
     */
    public void initAreaRatioByThreeMap() {
        areaRatioByThreeMap = new HashMap<>(3);
        areaRatioByThreeMap.put(1, 0);
        areaRatioByThreeMap.put(2, 0);
        areaRatioByThreeMap.put(3, 0);
        for (int i = 0; i < numLength; i++) {
            int num = originFrontNumber[i];
            int area = NumberUtil.getAreaByThree(num);
            areaRatioByThreeMap.put(area, areaRatioByThreeMap.get(area) + 1);
        }
    }

    public void initRemainderByThreeMap() {
        remainderByThreeMap = new HashMap<>(3);
        remainderByThreeMap.put(0, 0);
        remainderByThreeMap.put(1, 0);
        remainderByThreeMap.put(2, 0);
        for (int i = 0; i < numLength; i++) {
            int num = originFrontNumber[i];
            int area = NumberUtil.getRemainderByThree(num);
            remainderByThreeMap.put(area, remainderByThreeMap.get(area) + 1);
        }
    }

    /**
     * 获取首位差值对
     *
     * @return 首位差值对
     */
    public int[] getShouWeiChaNumbers() {
        List<Integer> set1 = NumberUtil.getShouWeiChaNumList(originFrontNumber);
        int[] result = new int[set1.size()];
        for (int i = 0; i < set1.size(); i++) {
            result[i] = set1.get(i);
        }
        return result;
    }

    /**
     * 获取本期前区号码的尾所对应的号码
     *
     * @return
     */
    public int[] getCurrDataTailNumber() {
        Set<Integer> set = new HashSet<Integer>(25);
        for (int num : originFrontNumber) {
            set.add(NumberUtil.getTail(num));
        }
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (Integer i : set) {
            numbers.addAll(NumberUtil.getTailNumMap().get(i));
        }
        int len = numbers.size();
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = numbers.get(i);
        }
        return result;
    }

    /**
     * 杀号
     *
     * @return
     */
    public int[] getKills() {
        ArrayList<Integer> killList = new ArrayList<Integer>();
        int i3 = originFrontNumber[2];
        // 绝杀第三位个位十位相加
        killList.add((i3 / 10 + i3 % 10));

        // 绝杀尾和
        int tailSum = 0;
        for (int i : originFrontNumber) {
            tailSum += i % 10;
        }
        // 杀尾和除以2
        int tailSumHalf = (int) Math.floor(tailSum / 2);
        killList.add(tailSum);
        killList.add(tailSumHalf);

        return NumberUtil.collectionToIntArray(killList);
    }


    /**
     * 获取斜连号
     *
     * @return 斜连号
     */
    public int[] getXieLian() {
        ArrayList<Integer> xlNum = new ArrayList<Integer>();
        for (int i = 0; i < originFrontNumber.length; i++) {
            int num = originFrontNumber[i];
            if (num - 1 >= 1) {
                xlNum.add(num - 1);
            }
            if (num + 1 <= 35) {
                xlNum.add(num + 1);
            }
        }
        int len = xlNum.size();
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = xlNum.get(i);
        }
        return result;
    }

    public Map<Integer, Integer> getAreaRatioByThreeMap() {
        return areaRatioByThreeMap;
    }

    public Map<Integer, Integer> getRemainderByThreeMap() {
        return remainderByThreeMap;
    }

    public int[] getOriginFrontNumber() {
        return originFrontNumber;
    }

    public static RowData valueOf(int[] arr) {
        return new RowData(arr);
    }

    /* ************************************************************************************************************* */

    private StringBuilder formattedNum = new StringBuilder("");
    private final DecimalFormat format = new DecimalFormat("00");

    private void format() {
        for (int num : originFrontNumber) {
            formattedNum.append(format.format(num)).append(",");
        }
    }

    @Override
    public String toString() {
        return formattedNum.toString();
    }
}
