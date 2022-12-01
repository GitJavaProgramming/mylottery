package org.pp;

import base.GLog;
import org.pp.component.table.CGridTable;
import org.pp.component.table.CellAlignEnum;
import org.pp.component.table.ColumnInfo;
import org.pp.handler.WindowEventHandler;
import org.pp.model.IssueRowData;
import org.pp.panel.ConditionFilterPanel;
import org.pp.panel.FilterNumberPanel;
import org.pp.panel.LocationSettingPanel;
import org.pp.panel.PropertyConditionPanel;
import org.pp.spider.FetchUtil;
import org.pp.util.ConfigUtil;
import org.pp.util.NumberUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    private JEditorPane editorPane = new JEditorPane();

    private int latestSelectedIndex = 3;
//    private volatile boolean isSet = false;

    private CGridTable<IssueRowData> gridTable;

    public MainFrame() {
        GLog.logger.info("MainFrame init start...");
//        this.setUndecorated(true); // 去掉窗口的装
//        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE); //采用指定的窗口装饰风格
        this.setTitle("大乐透分析");
        this.setLayout(new BorderLayout());
        initContentPane();

        tabbedPane = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        addTabbedPanel();
        tabbedPane.setSelectedIndex(latestSelectedIndex);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        this.pack();
        this.setResizable(false);
        this.addWindowListener(new WindowEventHandler());

        this.setLocationRelativeTo(null);
        this.setVisible(true);
        GLog.logger.info("MainFrame init end...");
    }

    private void addTabbedPanel() {
        tabbedPane.addTab("历史开奖数据", initHistoryNumberPanel());
        tabbedPane.addTab("基本属性>>", PropertyConditionPanel.getInstance());
        tabbedPane.addTab("条件过滤>>", ConditionFilterPanel.getInstance());
        tabbedPane.addTab("按位设置>>", LocationSettingPanel.getInstance());
        tabbedPane.addTab("过滤缩水", new FilterNumberPanel());

        tabbedPane.addPropertyChangeListener("MainFrame.tabbedPane.change", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
//                GLog.logger.info("MainFrame.tabbedPane.change:{}->{}", evt.getOldValue(), evt.getNewValue());
            }
        });
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabbedPane1 = (JTabbedPane) e.getSource();
                String title = tabbedPane1.getTitleAt(latestSelectedIndex);
                int selectedTab = tabbedPane1.getSelectedIndex();
                if ("基本属性>>".equals(title)) {
                    PropertyConditionPanel propertyConditionPanel = PropertyConditionPanel.getInstance();
                    if (propertyConditionPanel.action()) {
                        tabbedPane.firePropertyChange("MainFrame.tabbedPane.change", latestSelectedIndex, selectedTab);
                        latestSelectedIndex = selectedTab;
                    } else {
                        tabbedPane.setSelectedIndex(latestSelectedIndex);
                        JOptionPane.showMessageDialog(MainFrame.this, "条件构造错误，请检查！");
                        throw new RuntimeException("条件构造错误，请检查！");
                    }
                } else {
                    tabbedPane.firePropertyChange("MainFrame.tabbedPane.change", latestSelectedIndex, selectedTab);
                    latestSelectedIndex = selectedTab;
                }
            }
        });
    }

    private JPanel initHistoryNumberPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
//        panel.setPreferredSize(new Dimension(400, 600));

        panel.add(numberPanel(), BorderLayout.WEST);
        panel.add(centerPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane numberPanel() {
        NumberUtil.fillEditorPane(editorPane, ConfigUtil.getLoadNumber());
        editorPane.setEditable(false);
        editorPane.setEnabled(false);
//        editorPane.getDocument().addDocumentListener();
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setFocusCycleRoot(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "历史开奖数据", TitledBorder.CENTER, TitledBorder.TOP));
        scrollPane.setPreferredSize(new Dimension(260, 600));

        return scrollPane;
    }

    private JPanel centerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton updateBtn = new JButton("更新数据");
//        updateBtn.setBounds(0, 0, 100, 20);
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridTable.clear();
                java.util.List<Map<String, Object>> list1 = FetchUtil.update2();
                java.util.List<IssueRowData> dataList = new ArrayList<>();
                for (Map<String, Object> lotteryDraw : list1) {
//                    private String lotteryDrawNum;
//                    private String lotteryDrawResult;
                    dataList.add(new IssueRowData(Integer.parseInt(lotteryDraw.get("lotteryDrawNum").toString()), lotteryDraw.get("lotteryDrawResult").toString()));
                }
                gridTable.setData(dataList);
                NumberUtil.doUpdateFile();
                NumberUtil.fillEditorPane(editorPane, ConfigUtil.getLoadNumber());
            }
        });
        panelNorth.add(updateBtn);
        panel.add(panelNorth, BorderLayout.NORTH);
        panel.add(tablePanel(), BorderLayout.CENTER);
        return panel;
    }

    private void updateTable(java.util.List<int[]> list) {
        java.util.List<IssueRowData> dataList = new ArrayList<>();
//        java.util.List<int[]> list = FetchUtil.update();
        for (int i = 0; i < list.size(); i++) {
            int[] issue = list.get(i);
            IssueRowData issueRowData = new IssueRowData();
            issueRowData.setIssue(issue[0]);

            StringBuilder sb = new StringBuilder("");
            for (int num = 1; num < issue.length; num++) {
                sb.append(NumberUtil.DF2.format(issue[num])).append(" ");
            }
            String number = sb.substring(0, sb.length() - 1).toString();
            issueRowData.setNumber(number);

            dataList.add(issueRowData);
        }
        Collections.reverse(dataList);
        gridTable.setData(dataList);
    }

    private JPanel tablePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "最近十五期开奖号", TitledBorder.CENTER, TitledBorder.TOP));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().setView(initGridTable());

        panel.add(scrollPane);
        return panel;
    }

    private CGridTable<IssueRowData> initGridTable() {
        if (gridTable == null) {
            gridTable = new CGridTable<>(ColumnInfo.createIndexColumn());

            ColumnInfo c2 = new ColumnInfo("issue", "期号").setAlign(CellAlignEnum.center);
            ColumnInfo c3 = new ColumnInfo("number", "开奖号").setAlign(CellAlignEnum.center).setWidthFixed(true);
            gridTable.addColumn(c2, 100);
            gridTable.addColumn(c3, 300);

            java.util.List<int[]> list = FetchUtil.getResult();
            updateTable(list);
        }
        return gridTable;
    }

    public void initContentPane() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        setContentPane(panel);
    }
}
