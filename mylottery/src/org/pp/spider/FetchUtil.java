package org.pp.spider;

import base.GLog;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import practice.util.DataUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-4-17
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
public class FetchUtil {
    private static final String url = "http://chart.lottery.gov.cn//dltBasicZongHeTongJi.do?typ=3&issueTop=15&param=0";
    private static final String url2 = "http://chart.lottery.gov.cn//dltBasicZongHeTongJi.do?typ=3&issueTop=5000&param=0";

    private static List<int[]> result = new ArrayList<>();

    public static List<int[]> updateAll() {
        GLog.logger.info("抓取体彩中心数据 start...");
        Connection con = Jsoup.connect(url2);
        //建立链接，需要爬取的网页
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        //伪造浏览器请求
        Connection.Response rs = null;//建立链接
        try {
            rs = con.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        String str = d1.body().ownText();//转换成String类型
//        开奖日期 期号 开奖号码 和数值 跨度值 最大号码 最小号码 和尾值 平均值 合跨和 合跨差 尾数和值 连号个数 连号组数 尾数组数
        // content fromIndex = 15  tailIndex = 27
        String[] array0 = str.split(" |-");
        int len = array0.length - 15 - 27;
        String[] array = new String[len];
        System.arraycopy(array0, 15, array, 0, len);
        int mod = 3;
        List<int[]> list = new ArrayList<>();
        for (int i = 1; i < len; i++) {
            int tempMod = i % 23;
            if (tempMod == mod) {
                int[] intArr = new int[8];
                intArr[0] = DataUtils.asInt(array[i].substring(2, 7));
                for (int j = 1; j < 8; j++) {
                    intArr[j] = DataUtils.asInt(array[i + j]);
                }
                list.add(intArr);
            }
        }
        setResult(list);
        GLog.logger.info("抓取体彩中心数据 end...");
        return list;
    }

    public static List<int[]> update() {
        GLog.logger.info("抓取体彩中心数据 start...");
        Connection con = Jsoup.connect(url);
        //建立链接，需要爬取的网页
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        //伪造浏览器请求
        Connection.Response rs = null;//建立链接
        try {
            rs = con.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        String str = d1.body().ownText();//转换成String类型
//        开奖日期 期号 开奖号码 和数值 跨度值 最大号码 最小号码 和尾值 平均值 合跨和 合跨差 尾数和值 连号个数 连号组数 尾数组数
        // content fromIndex = 15  tailIndex = 27
        String[] array0 = str.split(" |-");
        int len = array0.length - 15 - 27;
        String[] array = new String[len];
        System.arraycopy(array0, 15, array, 0, len);
        int mod = 3;
        List<int[]> list = new ArrayList<>();
        for (int i = 1; i < len; i++) {
            int tempMod = i % 23;
            if (tempMod == mod) {
                int[] intArr = new int[8];
                intArr[0] = DataUtils.asInt(array[i].substring(2, 7));
                for (int j = 1; j < 8; j++) {
                    intArr[j] = DataUtils.asInt(array[i + j]);
                }
                list.add(intArr);
            }
        }
        setResult(list);
        GLog.logger.info("抓取体彩中心数据 end...");
        return list;
    }

    public static List<int[]> getResult() {
        return result;
    }

    public static void setResult(List<int[]> result) {
        FetchUtil.result = result;
    }

    public static void main(String[] args) {
        List<int[]> list = FetchUtil.update();
    }
}
