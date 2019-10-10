package org.pp.panel;

import org.apache.commons.lang3.StringUtils;
import org.pp.filter.PropertyConditionFilter;
import org.pp.model.Pair;
import practice.util.DataUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;

public class PropertyConditionPanel extends CPanel {

    private static PropertyConditionPanel instance = new PropertyConditionPanel();

    private PropertyConditionFilter condition = new PropertyConditionFilter();
    /* 除三余数panel*/
    private JPanel remainderByThreePanel;
    private Map<Integer, Set<Integer>> m1 = new HashMap(3);
    /* 三区比panel*/
    private JPanel threeAreaRatioPanel;
    private Map<Integer, Set<Integer>> m2 = new HashMap(3);
    /* 三区比panel2*/
    private JPanel threeAreaRatioPanel2;

    /* 生成过滤条件 */
    private JLabel remainderByThreeLabel = new JLabel();
    private JLabel threeAreaRatioLabel = new JLabel();

    private JTextField minSumField = new JTextField("50", 3);
    private JTextField maxSumField = new JTextField("120", 3);
    private JLabel tipLabel;

    private PropertyConditionPanel() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(super.getWidth(), super.getHeight()));

        add(getWestPanel(), BorderLayout.WEST);
        add(getCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel getCenterPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(initOtherCondition());
        return panel;
    }

    private JPanel getWestPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(450, getHeight() - 1));
        panel.add(initRemainderByThreePanel());
        panel.add(initThreeAreaRatioPanel2());
//        panel.add(initOtherCondition());
        return panel;
    }

    private JPanel initRemainderByThreePanel() {
        if (remainderByThreePanel == null) {
            remainderByThreePanel = new JPanel();
            remainderByThreePanel.setPreferredSize(new Dimension(440, 140));
            remainderByThreePanel.setLayout(new GridLayout(3, 1));
            remainderByThreePanel.setBorder(BorderFactory.createTitledBorder("除3余数类型"));

            for (int i = 0; i < 3; i++) {
                JPanel p2 = new JPanel();
                p2.setName("" + i);
                p2.setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel label = new JLabel("除3余" + i + "个数：", SwingConstants.RIGHT);
                label.setPreferredSize(new Dimension(100, 22));
                p2.add(label);
                for (int j = 0; j <= 5; j++) {
                    final JCheckBox checkBox = new JCheckBox("" + j);
                    p2.add(checkBox);
                    checkBox.addItemListener(new ItemListenerHandler(m1));
                }
                remainderByThreePanel.add(p2);
            }
        }
        return remainderByThreePanel;
    }

    private JPanel initThreeAreaRatioPanel2() {
        if (threeAreaRatioPanel2 == null) {
            threeAreaRatioPanel2 = new JPanel();
            threeAreaRatioPanel2.setPreferredSize(new Dimension(440, 200));
            threeAreaRatioPanel2.setLayout(new GridLayout(6, 1));
            threeAreaRatioPanel2.setBorder(BorderFactory.createTitledBorder("三区比"));

            for (int i = 0; i <= 5; i++) {
                JPanel p2 = new JPanel();
                p2.setLayout(new FlowLayout(FlowLayout.LEFT));
                for (int j = 0; j <= 5; j++) {
                    for (int k = 0; k <= 5; k++) {
                        if ((i + j + k) == 5) {
                            final JCheckBox checkBox = new JCheckBox(i + ":" + j + ":" + k);
                            p2.add(checkBox);
                            checkBox.addItemListener(new ItemListenerHandler02(condition, condition.getThreeAreaRatioList()));
                        }
                    }
                }
                threeAreaRatioPanel2.add(p2);
            }
        }
        return threeAreaRatioPanel2;
    }

    class ItemListenerHandler02 implements ItemListener {

        private final PropertyConditionFilter condition;

        private final List<String> list;

        public ItemListenerHandler02(PropertyConditionFilter condition, List<String> list) {
            this.condition = condition;
            this.list = list;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox c = (JCheckBox) e.getSource();
            String value = c.getText();
            if (c.isSelected()) {
                list.add(value);
            } else {
                list.remove(value);
            }
        }
    }

    private JPanel initThreeAreaRatioPanel() {
        if (threeAreaRatioPanel == null) {
            threeAreaRatioPanel = new JPanel();
            threeAreaRatioPanel.setPreferredSize(new Dimension(440, 140));
            threeAreaRatioPanel.setLayout(new GridLayout(3, 1));
            threeAreaRatioPanel.setBorder(BorderFactory.createTitledBorder("三区比"));

            for (int i = 0; i < 3; i++) {
                JPanel p2 = new JPanel();
                p2.setName("" + (i + 1)); // 设置panel名称 用于子组件checkbox识别
                p2.setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel label = new JLabel((i + 1) + "区个数：", SwingConstants.RIGHT);
                label.setPreferredSize(new Dimension(100, 22));
                p2.add(label);
                for (int j = 0; j <= 5; j++) {
                    final JCheckBox checkBox = new JCheckBox("" + j);
                    p2.add(checkBox);
                    checkBox.addItemListener(new ItemListenerHandler(m2));
                }
                threeAreaRatioPanel.add(p2);
            }
        }
        return threeAreaRatioPanel;
    }

    /**
     * 除三余数 三区比选择事件处理
     */
    class ItemListenerHandler implements ItemListener {

        private final Map<Integer, Set<Integer>> model;

        public ItemListenerHandler(Map<Integer, Set<Integer>> model) {
            this.model = model;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox c = (JCheckBox) e.getSource();
            Integer count = DataUtils.asInt(c.getText());
            String parentName = c.getParent().getName();
            int key = DataUtils.asInt(parentName);
            Set<Integer> set = model.get(key);
            if (set == null) {
                set = new HashSet<>(5);
                model.put(key, set);
            }
            if (c.isSelected()) {
                set.add(count);
            } else {
                set.remove(count);
            }
//            remainderByThreeLabel.setText(m1.toString());
//            threeAreaRatioLabel.setText(m2.toString());
//            threeAreaRatioLabel.setText(buildCondition().toString());
        }
    }

    private JPanel initOtherCondition() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(450, 410));
        panel.setLayout(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createTitledBorder("其他过滤项"));

        panel.add(createSumPanel("和值范围"));
        panel.add(createACPanel("ac值"));
        panel.add(createCommonRatioPanel("大小比"));
        panel.add(createCommonRatioPanel("奇偶比"));
        panel.add(createCommonRatioPanel("质合比"));
        panel.add(createPanel4Count("首尾差值尾", new ItemSelectedHandler01(condition, condition.getChaTailList()), 3));
        panel.add(createPanel4Count("上期出现尾", new ItemSelectedHandler01(condition, condition.getSqcxwList()), 4));
        panel.add(createPanel4Count("重号", new ItemSelectedHandler01(condition, condition.getChongHaoList()), 3));
        panel.add(createPanel4Count("隔号", new ItemSelectedHandler01(condition, condition.getGeHaoList()), 3));
        panel.add(createPanel4Count("重号隔号总数", new ItemSelectedHandler01(condition, condition.getChongHaoGeHaoList()), 3));
        panel.add(createPanel4Count("孤号", new ItemSelectedHandler01(condition, condition.getGuHaoList()), 1, 5));
        panel.add(createPanel4Count("邻号", new ItemSelectedHandler01(condition, condition.getXlHaoList()), 3));
        panel.add(createPanel4Count("角号", new ItemSelectedHandler01(condition, condition.getJiaoHaoList()), 0, 3));
        panel.add(createPanel4Count("围号", new ItemSelectedHandler01(condition, condition.getWeiHaoList()), 2, 5));
        return panel;
    }

    private JPanel createSumPanel(String name) {
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.setName(name);
        JLabel label = new JLabel(name + ":", SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(100, 22));
        p1.add(label);
        p1.add(minSumField);
        minSumField.setPreferredSize(new Dimension(20, 22));
        minSumField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        p1.add(new JLabel("-"));
        p1.add(maxSumField);
        maxSumField.setPreferredSize(new Dimension(20, 22));
        tipLabel = new JLabel();
        p1.add(tipLabel);
        maxSumField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        return p1;
    }

    private void filter(DocumentEvent e) {
        final Document document = e.getDocument();
        final int endOffset = document.getLength();
        try {
            final String text = document.getText(0, endOffset);
            if (StringUtils.isNumeric(text)) {
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            document.remove(text.length() - 1, 1);
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                tipLabel.setText("请输入数字，默认和值范围在40-130之间");
//                JOptionPane.showMessageDialog(SwingUtil.getParentWindow(minSumField), "请输入数字40-130之间");
            }
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    private JPanel createPanel4Count(String name, ItemListener itemListener, int sum) {
        return createPanel4Count(name, itemListener, 0, sum);
    }

    private JPanel createPanel4Count(String name, ItemListener itemListener, int start, int sum) {
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.setName(name);
        JLabel label = new JLabel(name + ":", SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(100, 22));
        p1.add(label);
        for (int i = start; i <= sum; i++) {
            JCheckBox checkBox = new JCheckBox(String.valueOf(i));
            checkBox.addItemListener(itemListener);
            p1.add(checkBox);
        }
        return p1;
    }

    class ItemSelectedHandler01 implements ItemListener {


        private final PropertyConditionFilter condition;

        private final List<Integer> list;

        public ItemSelectedHandler01(PropertyConditionFilter condition, List<Integer> list) {
            this.condition = condition;
            this.list = list;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox c = (JCheckBox) e.getSource();
            String value = c.getText();
//            List<Integer> acList = condition.getChaTailList();
            if (c.isSelected()) {
                list.add(DataUtils.asInt(value));
            } else {
                list.remove(Integer.valueOf(value));
            }
        }
    }

    private JPanel createACPanel(String name) {
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.setName(name);
        JLabel label = new JLabel(name + ":", SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(100, 22));
        p1.add(label);
        for (int i = 2; i <= 6; i++) {
            JCheckBox checkBox = new JCheckBox(String.valueOf(i));
            checkBox.addItemListener(new ItemSelectedHandlerForAC(condition));
            p1.add(checkBox);
        }
        return p1;
    }

    class ItemSelectedHandlerForAC implements ItemListener {


        private final PropertyConditionFilter condition;

        public ItemSelectedHandlerForAC(PropertyConditionFilter condition) {
            this.condition = condition;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox c = (JCheckBox) e.getSource();
            String value = c.getText();
            List<Integer> acList = condition.getAcList();
            if (c.isSelected()) {
                acList.add(DataUtils.asInt(value));
            } else {
                acList.remove(Integer.valueOf(value));
            }
        }
    }

    private JPanel createCommonRatioPanel(String name) {
        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.setName(name);
        JLabel label = new JLabel(name + ":", SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(100, 22));
        p1.add(label);
        for (int i = 0; i <= 5; i++) {
            StringBuilder sb = new StringBuilder("");
            sb.append(i).append(":").append((5 - i));
            JCheckBox checkBox = new JCheckBox(sb.toString());
            checkBox.addItemListener(new ItemSelectedHandler(condition));
            p1.add(checkBox);
        }
        return p1;
    }

    class ItemSelectedHandler implements ItemListener {


        private final PropertyConditionFilter condition;

        public ItemSelectedHandler(PropertyConditionFilter condition) {
            this.condition = condition;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox c = (JCheckBox) e.getSource();
            String ration = c.getText();
            String key = c.getParent().getName();
            Map<String, Set<String>> map = condition.getRationMap();
            Set<String> set = map.get(key);
            if (set == null) {
                set = new HashSet<>(5);
                map.put(key, set);
            }
            if (c.isSelected()) {
                set.add(ration);
            } else {
                set.remove(ration);
            }
        }
    }

    public PropertyConditionFilter buildCondition() {
        int minSum = DataUtils.asInt(minSumField.getText());
        int maxSum = DataUtils.asInt(maxSumField.getText());
        Pair pair = new Pair<Integer, Integer>(minSum, maxSum);
        return condition.build(m1, m2).setSumPair(pair);
    }

    public boolean action() {
        int minSum = DataUtils.asInt(minSumField.getText());
        int maxSum = DataUtils.asInt(maxSumField.getText());
        return minSum <= maxSum;
    }

    public static PropertyConditionPanel getInstance() {
        return instance;
    }
}
