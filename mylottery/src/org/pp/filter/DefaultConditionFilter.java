package org.pp.filter;

import base.GLog;
import org.pp.util.NumberUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-20
 * Time: 下午5:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class DefaultConditionFilter implements ConditionFilter {

    protected void initCondition(String name, List<String> condition) {
        try {
            String classPath = NumberUtil.getClassPath();
//            String fileName = System.getProperty("user.dir") + File.separator + name;
            String fileName = classPath + name;
            cacheCondition(fileName, condition);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void cacheCondition(String fileName, List<String> condition) throws IOException, URISyntaxException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = null;
        //按行读，并把每次读取的结果保存在line字符串中
        while ((line = reader.readLine()) != null) {
            if (line.matches("\\s*") || line.startsWith("#")) {
                continue;
            }
            condition.add(line.replaceAll("\\[| |]", ""));
        }
        reader.close();
    }

    protected boolean compareCondition(List<String> condition, int[] array) {
        return compareCondition(condition, array, false);
    }

    private boolean compareCondition(List<String> condition, int[] array, boolean isTest) {
        List<String> infoList = new LinkedList<>();
        String className = getClass().getSimpleName();
        if (isTest) {
            setInfoList(infoList);
            infoList.add(className + "开始检测...");
            GLog.logger.info("测试号码:{};CLASS:{}", Arrays.toString(array), className);
        }
        TreeSet<Integer> arrSet0 = NumberUtil.intArrayToTreeSet(array);
        int len = condition.size();
        boolean result = true;
        for (int i = 0; i < len; i++) {
            String line = condition.get(i);
            String[] splitCondition = line.split("-");
            TreeSet<Integer> set1 = NumberUtil.stringArrToTreeSet(splitCondition[0].split(","));//待选号码
            TreeSet<Integer> count = NumberUtil.stringArrToTreeSet(splitCondition[1].split(","));//号码出现个数

            TreeSet<Integer> arrSet = new TreeSet<Integer>(arrSet0);
            arrSet.retainAll(set1);
            boolean isContains = count.contains(arrSet.size());
            if (isTest) {
                if (isContains) {
                    continue;
                } else {
                    infoList.add("出错条件行：" + (i + 1) + "，号码：" + set1.toString().replaceAll("\\[| |]", "").replaceAll(",", " ") + "-" + count.toString().replaceAll("\\[| |]", "").replaceAll(",", " "));
                    System.out.println("出错条件行：" + (i + 1) + "，号码：" + set1.toString().replaceAll("\\[| |]", "").replaceAll(",", " ") + "-" + count.toString().replaceAll("\\[| |]", "").replaceAll(",", " "));
                    result = false;
                }
            } else {
                if (!isContains) {
                    return false;
                }
            }
        }
        if (isTest) {
            infoList.add(className + "检验完成");
            GLog.logger.info("{}检验完成", className);
        }
        return result;
    }

    public boolean filter(int[] array) {
        return filter(array, false);
    }

    public void testFilter(int[] array) {
        filter(array, true);
    }

    @Override
    public boolean filter(int[] array, boolean isTest) {
        return compareCondition(getCondition(), array, isTest);
    }
}
