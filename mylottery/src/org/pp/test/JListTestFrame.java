package org.pp.test;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.pp.component.CRoundedLineBorder;
import org.pp.component.list.CList;
import org.pp.component.list.listCellRenderer.CheckRenderer;
import org.pp.handler.WindowEventHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class JListTestFrame extends JFrame {

    String[] c = {"中国", "美国", "英国", "俄罗斯", "新加坡", "澳大利亚"};
    Vector<String> vector;

    {
        vector = new Vector<String>();
        vector.add("chrome");
        vector.add("firefox");
        vector.add("ie");
        vector.add("edge");
        vector.add("safari");
    }

    public JListTestFrame() {
        this.setTitle("JListTestFrame");
        this.setPreferredSize(new Dimension(700, 600));
        this.setLayout(new FlowLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CRoundedLineBorder(SystemColor.desktop));
        setContentPane(panel);

        addJListPanel();

        this.pack();
        this.setLocationRelativeTo(null);
        // 窗口事件
        this.addWindowListener(new WindowEventHandler());
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行钩子。");
            }
        }));

        this.setVisible(true);
    }


    private void addJListPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 300));
        CList list1 = new CList(vector);
//        list1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(list1));
        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        //设置皮肤，搜索关键字beautyeye下载相关的jar包
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();

            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
        }

        JListTestFrame frame = new JListTestFrame();
    }
}
