package org.pp.filter;

import base.GLog;
import org.pp.model.Pair;
import org.pp.model.RowData;
import org.pp.model.WrappedNumber;
import org.pp.util.NumberUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-10-8
 * Time: 下午5:42
 * To change this template use File | Settings | File Templates.
 */
public class LocationFilter extends DefaultConditionFilter {

    private List<String> infoList;

    private List<String> condition = new ArrayList<>();

    private Map<Pair, List<String>> pairMap = new HashMap<>();

    @Override
    public List<String> getCondition() {
        return condition;
    }

    @Override
    public List<String> getInfoList() {
        return infoList;
    }

    @Override
    public void setInfoList(List<String> infoList) {
        this.infoList = infoList;
    }

    public void setCondition(List<String> condition) {
        this.condition = condition;
    }

    public Map<Pair, List<String>> getPairMap() {
        return pairMap;
    }

    public void setPairMap(Map<Pair, List<String>> pairMap) {
        this.pairMap = pairMap;
    }

    @Override
    public boolean filter(int[] array, boolean isTest) {
        List<String> infoList = new LinkedList<>();
        String className = getClass().getSimpleName();
        if (isTest) {
            setInfoList(infoList);
            infoList.add(className + "开始检测...");
            GLog.logger.info("测试号码:{};CLASS:{}", Arrays.toString(array), className);
        }
        RowData rowData = RowData.valueOf(array);
        int[] originData = rowData.getOriginFrontNumber();
        int len = originData.length;
//        WrappedNumber[] frontNumber = new WrappedNumber[5];// 前区号码
        boolean flag = true;
        boolean flag2 = true;
        boolean flag3 = true;
        for (int i = 0; i < len; i++) {
            int num = originData[i];
            WrappedNumber wrappedNumber = WrappedNumber.valueOf(num); // 数组保存索引，wrappedNumber保存数据
            Pair pair1 = new Pair("奇偶属性", i+1);
            List<String> list1 = pairMap.get(pair1);
            if(list1 == null || list1.isEmpty()) {
                continue;
            }
            if(wrappedNumber.isOdd()) {
                if(list1.contains("奇数")) {
                    flag = flag & true;
                } else {
                    flag = flag & false;
                }
            } else {
                if(list1.contains("偶数")) {
                    flag = flag & true;
                } else {
                    flag = flag & false;
                }
            }
        }
        for (int i = 0; i < len; i++) {
            int num = originData[i];
            Pair pair2 = new Pair("除3余数", i+1);
            List<String> list2 = pairMap.get(pair2);
            if(list2 == null || list2.isEmpty()) {
                continue;
            }
            int remainderByThree = NumberUtil.getRemainderByThree(num);
            if(list2.contains(remainderByThree + "")) {
                flag2 = flag2 & true;
            } else {
                flag2 = flag2 & false;
            }
        }
        for (int i = 0; i < len; i++) {
            Pair pair1 = new Pair("质合属性", i+1);
            List<String> list1 = pairMap.get(pair1);
            if(list1 == null || list1.isEmpty()) {
                continue;
            }
            int num = originData[i];
            if(NumberUtil.isPrime(num)) {
                if(list1.contains("质数")) {
                    flag3 = flag3 & true;
                } else {
                    flag3 = flag3 & false;
                }
            } else {
                if(list1.contains("合数")) {
                    flag3 = flag3 & true;
                } else {
                    flag3 = flag3 & false;
                }
            }
        }

        return flag & flag2 & flag3;
    }
}
