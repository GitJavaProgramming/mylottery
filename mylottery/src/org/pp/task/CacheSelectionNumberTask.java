package org.pp.task;

import base.GLog;
import org.pp.filter.ConditionFilter;
import org.pp.filter.DefaultBuildConditionFilter;
import org.pp.filter.DefaultPipeline;
import org.pp.filter.Pipeline;
import org.pp.model.RowData;
import org.pp.spider.FetchUtil;
import org.pp.util.ByteUtil;
import org.pp.util.ConfigUtil;
import org.pp.util.FileUtil;
import org.pp.util.NumberUtil;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-26
 * Time: 下午1:37
 * To change this template use File | Settings | File Templates.
 */
public class CacheSelectionNumberTask implements Callable<List<RowData>> {

    private static List<RowData> result;
    private static FutureTask<List<RowData>> futureTask;

    private volatile static boolean isStart = false;

    private volatile int currIssue = 0;

    public static void start() {
        if (!isStart) {
            ExecutorService service = Executors.newSingleThreadExecutor();
            CacheSelectionNumberTask csnt = new CacheSelectionNumberTask();
            futureTask = new FutureTask(csnt);
            service.submit(futureTask);
            service.shutdown();
            isStart = true;
        }
    }

    public static List<RowData> getResult() {
        if (!isStart) {
            throw new IllegalStateException("必须启动才能拿结果，启动需要调用start()");
        }
        if (futureTask.isDone()) {
            return result;
        } else {
            JOptionPane.showMessageDialog(null, "Wait a moment.");
        }
        try {
            return futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isDone() {
        return futureTask.isDone();
    }

    @Override
    public List<RowData> call() throws Exception {
        if (result == null) {
            result = new ArrayList<>(323121);
        }
        long t1 = System.currentTimeMillis();
        String classPath = NumberUtil.getClassPath();
        byte[] bytes = FileUtil.readFile(new File(classPath + ConfigUtil.getVersionCurr()));
        int curr = 0;
        if (bytes.length > 0) {
            curr = ByteUtil.byte2Int(bytes);
        }
        // 是否是当前期版本，是否已经首次过滤完成
        GLog.logger.info("版本当前期：[{}]", curr);
        ArrayList<int[]> arrayList = DefaultBuildConditionFilter.getInstance().getArrayList();
        int length = arrayList.size();
        int[] latestLine = arrayList.get(length - 1);
        int curr1 = latestLine[0];
        GLog.logger.info("程序当前期：[{}]", curr1);
        GLog.logger.info("update issue start...");
        currIssue = updateIssue(curr1);
        GLog.logger.info("update issue end...");
        if ((currIssue != curr) /*&& isOpenFirstFilter*/) { // 不是当前期执行首次过滤，首次过滤会初始化当前期版本
            GLog.logger.info("执行首次过滤，首次过滤会同步当前期版本");
            cache(ConfigUtil.getFilterNumberPath(), false);
        } else {
            GLog.logger.info("当前期版本，直接读取首次过滤结果");
            Thread.yield();
            cache(ConfigUtil.getFilterMiddleResult(), true);
        }
        long t2 = System.currentTimeMillis();
        GLog.logger.info("缓存待选号码,总共{}行，用时：[{}秒]", result.size(), (t2 - t1) / 1000);
        return result;
    }

    private int updateIssue(int curr) {
        List<Map<String, Object>> list = FetchUtil.update2();
        int latest = Integer.parseInt(list.get(0).get("lotteryDrawNum").toString());
//        int len = list.size();
//        if (latest > curr) {
//            int index = -1;
//            for (int i = 0; i < len - 1; i++) {
//                int issue = list.get(i)[0];
//                if (curr == issue) {
//                    index = i;
//                    break;
//                }
//            }
//            List<int[]> list2 = list.subList(index + 1, len);
//            NumberUtil.appendFile(list2, ConfigUtil.getLoadNumber());
//            NumberUtil.doUpdateFile();
//            DefaultBuildConditionFilter.getInstance().getArrayList().addAll(list2);//更新号码list缓存
//        }
        return latest;
    }

    private void cache(String name, boolean firstFilterDone) {
        try {
            String classPath = NumberUtil.getClassPath();
            String fileName = classPath + name;
            cacheNumber(fileName, firstFilterDone);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件 缓存数据
     *
     * @param fileName
     * @throws java.io.IOException
     */
    private void cacheNumber(String fileName, boolean firstFilterDone) throws IOException {
        long t0 = System.currentTimeMillis();
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = null;
        final Pipeline pipeline = DefaultPipeline.getInstance();
        //按行读，并把每次读取的结果保存在line字符串中
        while ((line = reader.readLine()) != null) {
            if (line.matches("\\s*") || line.startsWith("#")) {
                continue;
            }
            String[] strArr = line.split(" ");
            int[] array = NumberUtil.stringArrToIntArray(strArr);
            if (!firstFilterDone) {
                boolean flag = true;
                List<ConditionFilter> filterList = pipeline.getFilterList();
                for (ConditionFilter filter : filterList) {
                    if (!filter.filter(array, false)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    RowData rowData = RowData.valueOf(array);
                    result.add(rowData);
                }
            } else { // 已完成首次过滤 直接读取首次过滤结果
                RowData rowData = RowData.valueOf(array);
                result.add(rowData);
            }
        }
        reader.close();
        if (!firstFilterDone) {
            final long t1 = System.currentTimeMillis();
            GLog.logger.info("首次过滤号码,总共{}行，过滤用时：[{}秒]", result.size(), (t1 - t0) / 1000);
            // 开一个线程写入首次过滤结果
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filePath = ConfigUtil.getFilterMiddleResult();
                    NumberUtil.writeFile2(result, filePath);
                    long t2 = System.currentTimeMillis();
                    GLog.logger.info("首次过滤号码,总共{}行，写入文件用时：[{}秒]", result.size(), (t2 - t1) / 1000);
                }
            }).start();

            // 当前期，用于判断是否重新生成默认的过滤条件
            String classpath = NumberUtil.getClassPath();
            FileUtil.writeFile(ByteUtil.int2Byte(currIssue), new File(classpath + ConfigUtil.getVersionCurr()));
            GLog.logger.info("写入版本当前期：[{}]", currIssue);
        }
    }
}
