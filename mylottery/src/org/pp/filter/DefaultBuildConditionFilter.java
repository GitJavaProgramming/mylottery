package org.pp.filter;

import base.GLog;
import org.pp.model.RowData;
import org.pp.util.ConfigUtil;
import org.pp.util.NumberUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DefaultBuildConditionFilter extends DefaultConditionFilter {

    private final ArrayList<int[]> arrayList = new ArrayList<>(4000);
    private static DefaultBuildConditionFilter instance = new DefaultBuildConditionFilter();

    private List<String> infoList;

    private List<String> result = new ArrayList<String>();

    private DefaultBuildConditionFilter() {
        // 读取往期开奖数据，生成过滤条件并写入文件
        cache(ConfigUtil.getLoadNumber());
        buildCondition();
        initCondition(ConfigUtil.getDefaultBuildCondition(), result);
    }

    public String buildCondition(String origin, String condition) {
        origin = origin.replaceAll("\\[| |]", "") + condition;
        return origin;
    }

    /**
     * 生成条件
     */
    public void buildCondition() {
        GLog.logger.info("build default condition...");
        int length = arrayList.size();
        int[] latestLine = arrayList.get(length - 1);
        int[] dataArray = new int[5];
        System.arraycopy(latestLine, 1, dataArray, 0, 5);
        RowData rowData = RowData.valueOf(dataArray);
        String shouWeiCha = (NumberUtil.intArrayToTreeSet(rowData.getShouWeiChaNumbers())).toString();// 首尾差
        shouWeiCha = buildCondition(shouWeiCha, "-0,1,2,3");
        String tailNum = NumberUtil.intArrayToTreeSet(rowData.getCurrDataTailNumber()).toString();//上期出现的尾
        tailNum = buildCondition(tailNum, "-0,1,2,3,4");
        String xl = NumberUtil.intArrayToTreeSet(rowData.getXieLian()).toString(); // 斜连号
        xl = buildCondition(xl, "-0,1,2,3");
        String dw = NumberUtil.intArrayToTreeSet(rowData.kuaDuDianWei()).toString(); // 跨度的点位
        dw = buildCondition(dw, "-0,1,2");
        // 重号隔号
        int[] preLatestLine = arrayList.get(length - 2);
        int[] dataArray2 = new int[5];
        System.arraycopy(preLatestLine, 1, dataArray2, 0, 5);
        String chongHao = NumberUtil.intArrayToTreeSet(dataArray).toString();
        String geHao = Arrays.toString(dataArray2);
        chongHao = buildCondition((chongHao + "," + geHao), "-0,1,2,3");

        // 获取绝杀号
        String kills = NumberUtil.intArrayToTreeSet(rowData.getKills()).toString();
        kills = buildCondition(kills, "-0,1");
//        result.add(shouWeiCha);
//        result.add(tailNum);
//        result.add(xl);
//        result.add(dw);
//        result.add(chongHao);
//        result.add(kills);

        NumberUtil.writeFile(ConfigUtil.getDefaultBuildCondition(), shouWeiCha, tailNum, xl, dw, chongHao, kills);
        GLog.logger.info("build default condition done.");
    }

    public void analysisArea(int fromIndex, int toIndex) {
//        int len = toIndex - fromIndex;
//        WrappedLineNumber[] arr = new WrappedLineNumber[len];
//        System.arraycopy(defaultCache, fromIndex, arr, 0, len);
    }

    private void cache(String name) {
        try {
            String classPath = NumberUtil.getClassPath();
            String fileName = classPath + name;
            cacheNumber(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件 缓存数据
     *
     * @param fileName
     * @throws IOException
     */
    private void cacheNumber(String fileName) throws IOException, URISyntaxException {
        FileReader fileReader = new FileReader(new File(new URI(fileName)));
        BufferedReader reader = new BufferedReader(fileReader);
        String line = null;
        //按行读，并把每次读取的结果保存在line字符串中
        while ((line = reader.readLine()) != null) {
            if (line.matches("\\s*") || line.startsWith("#")) {
                continue;
            }
            String[] strArr = line.split(" ");
            int[] array = NumberUtil.stringArrToIntArray(strArr);
            arrayList.add(array);
        }
        reader.close();
    }

    public static DefaultBuildConditionFilter getInstance() {
        return instance;
    }

    public ArrayList<int[]> getArrayList() {
        return arrayList;
    }

    public List<String> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<String> infoList) {
        this.infoList = infoList;
    }

    @Override
    public List<String> getCondition() {
        return result;
    }
}
