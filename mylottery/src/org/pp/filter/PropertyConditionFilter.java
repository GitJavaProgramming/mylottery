package org.pp.filter;

import base.GLog;
import org.pp.model.Pair;
import org.pp.model.RowData;
import org.pp.util.NumberUtil;

import java.util.*;

public class PropertyConditionFilter extends DefaultConditionFilter {

    private List<String> infoList;

    private List<String> condition = new ArrayList<>();

    /*除三余数*/
    private Map<Integer, Set<Integer>> RemainderByThree;
    /*三区比*/
    private Map<Integer, Set<Integer>> threeAreaRatio;
    /* 奇偶比 大小比 质合比 */
    private Map<String, Set<String>> rationMap = new HashMap<>();

    /* 三区比 */
    private List<String> threeAreaRatioList = new ArrayList<String>();

    /* ac值 */
    private List<Integer> acList = new ArrayList<>(5);

    /* 首尾差值尾出现的个数 */
    private List<Integer> chaTailList = new ArrayList<>();

    /* 上期出现尾出现的个数 */
    private List<Integer> sqcxwList = new ArrayList<>();

    private Pair<Integer, Integer> sumPair = new Pair<>(50, 120); // 默认和值范围 50-120

    /* 重号出现的个数 */
    private List<Integer> chongHaoList = new ArrayList<>();

    /* 隔号出现的个数 */
    private List<Integer> geHaoList = new ArrayList<>();

    /* 重号和隔号出现的个数 */
    private List<Integer> chongHaoGeHaoList = new ArrayList<>();

    /* 斜连号出现的个数 */
    private List<Integer> xlHaoList = new ArrayList<>();

    /* 孤号出现的个数 */
    private List<Integer> guHaoList = new ArrayList<>();

    /* 角号出现的个数 */
    private List<Integer> jiaoHaoList = new ArrayList<>();

    /* 围号出现的个数 */
    private List<Integer> weiHaoList = new ArrayList<>();

    public PropertyConditionFilter() {
    }

    public PropertyConditionFilter build(Map<Integer, Set<Integer>> remainderByThree, Map<Integer, Set<Integer>> threeAreaRatio) {
        this.RemainderByThree = remainderByThree;
        this.threeAreaRatio = threeAreaRatio;
        return this;
    }

    public List<Integer> getChongHaoGeHaoList() {
        return chongHaoGeHaoList;
    }

    public void setChongHaoGeHaoList(List<Integer> chongHaoGeHaoList) {
        this.chongHaoGeHaoList = chongHaoGeHaoList;
    }

    public Map<Integer, Set<Integer>> getRemainderByThree() {
        return RemainderByThree;
    }

    public Map<Integer, Set<Integer>> getThreeAreaRatio() {
        return threeAreaRatio;
    }

    public Map<String, Set<String>> getRationMap() {
        return rationMap;
    }

    public List<Integer> getAcList() {
        return acList;
    }

    public void setAcList(List<Integer> acList) {
        this.acList = acList;
    }

    public List<Integer> getChaTailList() {
        return chaTailList;
    }

    public void setChaTailList(List<Integer> chaTailList) {
        this.chaTailList = chaTailList;
    }

    public List<Integer> getChongHaoList() {
        return chongHaoList;
    }

    public void setChongHaoList(List<Integer> chongHaoList) {
        this.chongHaoList = chongHaoList;
    }

    public List<Integer> getGeHaoList() {
        return geHaoList;
    }

    public void setGeHaoList(List<Integer> geHaoList) {
        this.geHaoList = geHaoList;
    }

    public List<Integer> getGuHaoList() {
        return guHaoList;
    }

    public void setGuHaoList(List<Integer> guHaoList) {
        this.guHaoList = guHaoList;
    }

    public List<Integer> getXlHaoList() {
        return xlHaoList;
    }

    public void setXlHaoList(List<Integer> xlHaoList) {
        this.xlHaoList = xlHaoList;
    }

    public List<Integer> getJiaoHaoList() {
        return jiaoHaoList;
    }

    public void setJiaoHaoList(List<Integer> jiaoHaoList) {
        this.jiaoHaoList = jiaoHaoList;
    }

    public List<Integer> getWeiHaoList() {
        return weiHaoList;
    }

    public void setWeiHaoList(List<Integer> weiHaoList) {
        this.weiHaoList = weiHaoList;
    }

    public List<String> getThreeAreaRatioList() {
        return threeAreaRatioList;
    }

    public void setThreeAreaRatioList(List<String> threeAreaRatioList) {
        this.threeAreaRatioList = threeAreaRatioList;
    }

    public List<Integer> getSqcxwList() {
        return sqcxwList;
    }

    public void setSqcxwList(List<Integer> sqcxwList) {
        this.sqcxwList = sqcxwList;
    }

    public void reset() {
        this.RemainderByThree = null;
        this.threeAreaRatio = null;
        this.rationMap = new HashMap<>();
    }

    public Pair<Integer, Integer> getSumPair() {
        return sumPair;
    }

    public PropertyConditionFilter setSumPair(Pair<Integer, Integer> sumPair) {
        this.sumPair = sumPair;
        return this;
    }

    /**
     * 除三余数检测
     *
     * @param rowData
     * @return
     */
    private boolean check1(RowData rowData, boolean isTest) {
        Map<Integer, Set<Integer>> map1 = getRemainderByThree();
        boolean canPut = true;
        Map<Integer, Integer> remainderByThreeMap = rowData.getRemainderByThreeMap();
        Set<Map.Entry<Integer, Integer>> entrySet = remainderByThreeMap.entrySet();
        for (Map.Entry<Integer, Integer> ele : entrySet) {
            Integer key = ele.getKey();
            Set<Integer> set = map1.get(key);
            if (set == null || set.isEmpty()) {
                continue;
            }
            Integer value = ele.getValue();
            boolean flag = set.contains(value);
            if (!flag) {
                if (isTest) {
                    infoList.add("出错条件：除三余数设置=" + map1 + ";当前数据：" + remainderByThreeMap);
                }
                canPut = false;
                break;
            }
        }
        return canPut;
    }

    /**
     * 三区比检测
     *
     * @param rowData
     * @return
     */
    private boolean check2(RowData rowData, boolean isTest) {
        Map<Integer, Set<Integer>> map2 = getThreeAreaRatio();
        boolean canPut = true;
        Map<Integer, Integer> remainderByThreeMap = rowData.getAreaRatioByThreeMap();
        Set<Map.Entry<Integer, Integer>> entrySet = remainderByThreeMap.entrySet();
        for (Map.Entry<Integer, Integer> ele : entrySet) {
            Integer key = ele.getKey();
            Set<Integer> set = map2.get(key);
            if (set == null || set.isEmpty()) {
                continue;
            }
            Integer value = ele.getValue();
            boolean flag = set.contains(value);
            if (!flag) {
                if (isTest) {
                    infoList.add("出错条件：三区比设置=" + map2 + ";当前数据：" + remainderByThreeMap);
                }
                canPut = false;
                break;
            }
        }
        return canPut;
    }

    private boolean check3(RowData rowData, boolean isTest) {
        Map<String, Set<String>> map3 = getRationMap();
        String str1 = rowData.getBigNumRatio();
        Set<String> set = map3.get("大小比");
        boolean flag = true;
        if (!(set == null || set.isEmpty())) {
            if (!set.contains(str1)) {
                flag = false;
                if (isTest) {
                    infoList.add("出错条件：大小比=" + set + ";当前数据：" + str1);
                } else {
                    return flag;
                }
            }
        }
        String str2 = rowData.getOddNumRatio();
        set = map3.get("奇偶比");
        if (!(set == null || set.isEmpty())) {
            if (!set.contains(str2)) {
                flag = false;
                if (isTest) {
                    infoList.add("出错条件：奇偶比=" + set + ";当前数据：" + str2);
                } else {
                    return flag;
                }
            }
        }
        String str3 = rowData.getPrimerRatio();
        set = map3.get("质合比");
        if (!(set == null || set.isEmpty())) {
            if (!set.contains(str3)) {
                flag = false;
                if (isTest) {
                    infoList.add("出错条件：质合比=" + set + ";当前数据：" + str3);
                } else {
                    return flag;
                }
            }
        }
        return flag;
    }

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
        int sum = NumberUtil.sum(array);
        boolean sumFlag = true;
        if (sum < sumPair.getKey() || sum > sumPair.getValue()) {
            if (isTest) {
                infoList.add("出错条件：和值范围=" + sumPair + ", 当前和值=" + sum);
            } else {
                sumFlag = false;
                return sumFlag;
            }
        }
        int ac = rowData.ac();
        boolean acFlag = true;
        if (!acList.isEmpty() && !acList.contains(ac)) {
            if (isTest) {
                infoList.add("出错条件：ac值选项=" + acList + ", ac=" + ac);
            } else {
                acFlag = false;
                return acFlag;
            }
        }
        TreeSet<Integer> numSet = NumberUtil.intArrayToTreeSet(array); // 当前号码
        boolean chaTailFlag = true;
        if (!chaTailList.isEmpty()) {
            List<Integer> list = NumberUtil.getShouWeiChaNumList(array);
            list.retainAll(numSet);
            if (!chaTailList.contains(list.size())) {
                if (isTest) {
                    infoList.add("出错条件：首尾差值尾出现的个数=" + chaTailList + ", 实际出现个数：" + list.size());
                } else {
                    chaTailFlag = false;
                    return chaTailFlag;
                }
            }
        }
        boolean sqcxwTailFlag = true;
        if (!sqcxwList.isEmpty()) {
            List<Integer> list = NumberUtil.getLatestTailNumber();
            list.retainAll(numSet);
            if (!sqcxwList.contains(list.size())) {
                if (isTest) {
                    infoList.add("出错条件：上期出现尾出现的个数=" + sqcxwList + ", 实际出现个数：" + list.size());
                } else {
                    sqcxwTailFlag = false;
                    return sqcxwTailFlag;
                }
            }
        }
        boolean chongHaoFlag = true;
        if (!chongHaoList.isEmpty()) {
            int[] latestLine = NumberUtil.getLatestNum();
            int[] frontNumber = new int[5];
            System.arraycopy(latestLine, 1, frontNumber, 0, 5);
            TreeSet<Integer> treeSet = NumberUtil.intArrayToTreeSet(frontNumber);
            treeSet.retainAll(numSet);
            if (!chongHaoList.contains(treeSet.size())) {
                if (isTest) {
                    infoList.add("出错条件：重号个数=" + chongHaoList + ", 实际出现个数：" + treeSet.size());
                } else {
                    chongHaoFlag = false;
                    return chongHaoFlag;
                }
            }
        }
        boolean geHaoFlag = true;
        if (!geHaoList.isEmpty()) {
            int[] latestLine = NumberUtil.getSecondLatestNum();
            int[] frontNumber = new int[5];
            System.arraycopy(latestLine, 1, frontNumber, 0, 5);
            TreeSet<Integer> treeSet = NumberUtil.intArrayToTreeSet(frontNumber);
            treeSet.retainAll(numSet);
            if (!geHaoList.contains(treeSet.size())) {
                if (isTest) {
                    infoList.add("出错条件：隔号个数=" + geHaoList + ", 实际出现个数：" + treeSet.size());
                } else {
                    geHaoFlag = false;
                    return geHaoFlag;
                }
            }
        }
        boolean chonghaoGeHaoFlag = true;
        if (!chongHaoGeHaoList.isEmpty()) {
            // 隔号
            int[] latestLine = NumberUtil.getSecondLatestNum();
            int[] frontNumber = new int[5];
            System.arraycopy(latestLine, 1, frontNumber, 0, 5);
            TreeSet<Integer> treeSet = NumberUtil.intArrayToTreeSet(frontNumber);
            // 重号
            int[] latestLine2 = NumberUtil.getLatestNum();
            int[] frontNumber2 = new int[5];
            System.arraycopy(latestLine2, 1, frontNumber2, 0, 5);
            TreeSet<Integer> treeSet2 = NumberUtil.intArrayToTreeSet(frontNumber2);
            treeSet.addAll(treeSet2);
            treeSet.retainAll(numSet);
            if (!chongHaoGeHaoList.contains(treeSet.size())) {
                if (isTest) {
                    infoList.add("出错条件：重号隔号总数=" + chongHaoGeHaoList + ", 实际出现个数：" + treeSet.size());
                } else {
                    chonghaoGeHaoFlag = false;
                    return chonghaoGeHaoFlag;
                }
            }
        }
        boolean xlHaoFlag = true;
        if (!xlHaoList.isEmpty()) {
            Set<Integer> treeSet = NumberUtil.getLatestLinHaoNumber(); // 斜连号
            treeSet.retainAll(numSet);
            if (!xlHaoList.contains(treeSet.size())) {
                if (isTest) {
                    infoList.add("出错条件：邻号个数=" + xlHaoList + ", 实际出现个数：" + treeSet.size());
                } else {
                    xlHaoFlag = false;
                    return xlHaoFlag;
                }
            }
        }
        boolean guHaoFlag = true;
        if (!guHaoList.isEmpty()) {// 孤号 不是重号也不是邻号
            TreeSet<Integer> allNumSet = new TreeSet<>(NumberUtil.getNumberSet());
            int[] latestLine = NumberUtil.getLatestNum();
            int[] frontNumber = new int[5];
            System.arraycopy(latestLine, 1, frontNumber, 0, 5);
            TreeSet<Integer> chonghaoSet = NumberUtil.intArrayToTreeSet(frontNumber); // 重号
            allNumSet.removeAll(chonghaoSet);
            RowData data = RowData.valueOf(frontNumber);
            TreeSet<Integer> treeSet = NumberUtil.intArrayToTreeSet(data.getXieLian()); // 斜连号
            allNumSet.removeAll(treeSet);
            allNumSet.retainAll(numSet);
            if (!guHaoList.contains(allNumSet.size())) {
                if (isTest) {
                    infoList.add("出错条件：孤号个数=" + guHaoList + ", 实际出现个数：" + allNumSet.size());
                } else {
                    guHaoFlag = false;
                    return guHaoFlag;
                }
            }
        }

        boolean jiaoHaoFlag = true;
        if (!jiaoHaoList.isEmpty()) {
            TreeSet<Integer> treeSet = new TreeSet<>(NumberUtil.getJiaoNumberSet());
            treeSet.retainAll(numSet);
            if (!jiaoHaoList.contains(treeSet.size())) {
                if (isTest) {
                    infoList.add("出错条件：角号个数=" + jiaoHaoList + ", 实际出现个数：" + treeSet.size());
                } else {
                    jiaoHaoFlag = false;
                    return jiaoHaoFlag;
                }
            }
        }

        boolean weiHaoFlag = true;
        if (!weiHaoList.isEmpty()) {
            TreeSet<Integer> treeSet = new TreeSet<>(NumberUtil.getWeiNumberSet());
            treeSet.retainAll(numSet);
            if (!weiHaoList.contains(treeSet.size())) {
                if (isTest) {
                    infoList.add("出错条件：围号个数=" + weiHaoList + ", 实际出现个数：" + treeSet.size());
                } else {
                    weiHaoFlag = false;
                    return weiHaoFlag;
                }
            }
        }

        boolean threeAreaRatioFlag = true;
        if (!threeAreaRatioList.isEmpty()) {
            String threeAreaRatio = rowData.areaRatioByThree();
            if (!threeAreaRatioList.contains(threeAreaRatio)) {
                if (isTest) {
                    infoList.add("出错条件：三区比=" + threeAreaRatioList + ", 实际出现：" + threeAreaRatio);
                } else {
                    threeAreaRatioFlag = false;
                    return threeAreaRatioFlag;
                }
            }
        }

        boolean flag = chonghaoGeHaoFlag & sqcxwTailFlag & threeAreaRatioFlag & weiHaoFlag & jiaoHaoFlag & xlHaoFlag & guHaoFlag & geHaoFlag & chongHaoFlag & sumFlag & acFlag & chaTailFlag & check1(rowData, isTest)/* & check2(rowData, isTest)*/ & check3(rowData, isTest);
        if (isTest) {
            infoList.add(className + "检验完成");
            GLog.logger.info("{}检验完成", className);
        }
        return flag;
    }
}
