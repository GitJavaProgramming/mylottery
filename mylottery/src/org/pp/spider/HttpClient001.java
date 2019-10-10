package org.pp.spider;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;

public class HttpClient001 {
    private static final String url = "http://chart.lottery.gov.cn//dltBasicZongHeTongJi.do?typ=3&issueTop=10&param=0";

    public static void main(String[] args) throws IOException {

        Connection con = Jsoup.connect(url);
        //建立链接，需要爬取的网页
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        //伪造浏览器请求
        Connection.Response rs = con.execute();//建立链接
        Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
        String str = d1.body().ownText();//转换成String类型
//        System.out.println("2019042 04 10 13 28 33-11 12".length());  // 28
        str = str.substring(67, 747);
        System.out.println(str);
        String[] array = str.split(" |-");
        System.out.println(array[0].substring(2, 7));
    }
}
