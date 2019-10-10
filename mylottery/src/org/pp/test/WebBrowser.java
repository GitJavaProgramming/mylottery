package org.pp.test;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class WebBrowser extends JApplet {

    //使用JEditorPane类创建了一个简单地web浏览器
    /**
     * JEditorPane类能以HTML格式显示文件
     */
    private JEditorPane jep = new JEditorPane();
    private JLabel jlblURL = new JLabel("URL");
    private JTextField jtfURL = new JTextField();

    //初始化applet
    public void init() {
        JPanel jpURL = new JPanel();
        jpURL.setLayout(new BorderLayout());
        jpURL.add(jlblURL, BorderLayout.WEST);
        jpURL.add(jtfURL, BorderLayout.CENTER);

        //为了能过滚动，将编辑窗格放置到滚动窗格中
        JScrollPane jspViewer = new JScrollPane();
        jspViewer.getViewport().add(jep, null);

        //把jpUrl和jspViewer放在applet中
        add(jspViewer, BorderLayout.CENTER);
        add(jpURL, BorderLayout.NORTH);

        jep.setEditable(false);
        //当单机编辑窗格中的超链接时，JEditorPane产生javax.swing.event.HyperlinkEvent事件
        jep.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent arg0) {
                try {
                    URL url = new URL(jtfURL.getText().trim());
                    /**
                     * JEditorPane的setPage()方法可以显示url
                     */
                    jep.setPage(url);
                    System.out.println("+++++++++++++++++==");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        jtfURL.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    URL url = new URL(jtfURL.getText().trim());
                    jep.setPage(url);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });
    }

    /**
     * 主方法
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("web Browser");

        WebBrowser applet = new WebBrowser();
        frame.getContentPane().add(applet, BorderLayout.CENTER);

        applet.init();
        applet.start();

        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}