package org.pp.panel;

import base.GLog;
import org.apache.commons.lang3.StringUtils;
import org.pp.filter.*;
import org.pp.model.RowData;
import org.pp.spider.FetchUtil;
import org.pp.task.CacheSelectionNumberTask;
import org.pp.util.ConfigUtil;
import org.pp.util.FileUtil;
import org.pp.util.NumberUtil;
import org.pp.util.SwingUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class FilterNumberPanel extends CPanel {

    final JTextField textField = new JTextField(14);
    private List<RowData> result;
    private volatile boolean isSet = false;
    private JEditorPane editorPane = new JEditorPane();
    private JLabel labelCountAllNum;
    private JLabel labelAfterFilter;

    public FilterNumberPanel() {
        this.setPreferredSize(new Dimension(900, 600));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createDashedBorder(SystemColor.desktop), "选号结果", TitledBorder.CENTER, TitledBorder.TOP));
        this.setLayout(new BorderLayout());

        this.add(initControlPanel(), BorderLayout.WEST);
        this.add(filterNumberPanel(), BorderLayout.CENTER);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = true;
                while (flag) {
                    if (CacheSelectionNumberTask.isDone()) {
                        GLog.logger.info("task isDone, update UI.");
                        // 执行完成时，更新界面组件信息
                        labelAfterFilter.setText("" + CacheSelectionNumberTask.getResult().size());
                        NumberUtil.fillEditorPane(editorPane, ConfigUtil.getFilterMiddleResult());
                        int[] latestLine = NumberUtil.stringArrToIntArray(FetchUtil.issueList.get(0).get("lotteryDrawResult").toString().split(" "));
                        int[] frontNumber = new int[5];
                        System.arraycopy(latestLine, 0, frontNumber, 0, 5);
                        textField.setText(NumberUtil.IntArrayToString2(frontNumber));
                        flag = false;
                    }
                }
            }
        }).start();

    }

    private JPanel initControlPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 590));

        JLabel label1 = new JLabel("彩票组合数：", SwingConstants.RIGHT);
        labelCountAllNum = new JLabel("323121");
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(300, 30));
        p1.add(label1);
        p1.add(labelCountAllNum);

        JLabel label2 = new JLabel("过滤后号码注数：", SwingConstants.RIGHT);
        labelAfterFilter = new JLabel("" + 0);
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(300, 30));
        p2.add(label2);
        p2.add(labelAfterFilter);

        panel.add(p1);
        panel.add(p2);

        JButton button = new JButton("过滤缩水");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSet) {
                    isSet = true;
                    result = CacheSelectionNumberTask.getResult();
                }
                final int len = result.size();
//                final CProgressBar.CProgressDialog dlg = new CProgressBar.CProgressDialog(FilterNumberPanel.this, 0, len);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 动态条件过滤
                            DynamicConditionFilter dynamicConditionFilter = DynamicConditionFilter.getInstance(ConditionFilterPanel.getInstance().getSelectedCondition());
                            clearEditorPanel();
                            List<int[]> finalResult = new LinkedList();
                            // 属性过滤
                            PropertyConditionFilter propertyConditionFilter = PropertyConditionPanel.getInstance().buildCondition();
                            // 按位过滤
                            LocationFilter locationFilter = LocationSettingPanel.getInstance().buildFilter();

                            for (int i = 0; i < len; i++) {
                                RowData rowData = result.get(i);
                                int[] array = rowData.getOriginFrontNumber();

                                if (!propertyConditionFilter.filter(array)) {
                                    continue;
                                }

                                if (!dynamicConditionFilter.filter(array)) {
                                    continue;
                                }

                                if (locationFilter.filter(array)) {
                                    finalResult.add(array);
                                }

//                                dlg.setProgressValue(i);
//                                Thread.sleep(1);
                            }
                            labelAfterFilter.setText("" + finalResult.size());
                            NumberUtil.writeFile(finalResult, ConfigUtil.getFilterFinalResult());
                            NumberUtil.fillEditorPane(editorPane, ConfigUtil.getFilterFinalResult());
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(SwingUtil.getParentWindow(FilterNumberPanel.this), "过滤被取消。");
//                            dlg.setCanceled(true);
                        } finally {
//                            dlg.dispose();
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();
//                dlg.setRelateThread(thread);
//                dlg.setVisible(true);
            }
        });
        JButton button2 = new JButton("导出结果");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        JPanel p3 = new JPanel();
        p3.setPreferredSize(new Dimension(300, 60));
        p3.add(button);
        p3.add(button2);
        panel.add(p3);

        textField.setFont(new Font("宋体", Font.PLAIN, 18));
        final JLabel tipLabel = new JLabel();
        tipLabel.setPreferredSize(new Dimension(300, 30));
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                final Document document = e.getDocument();
                final int endOffset = document.getLength();
                try {
                    final String text = document.getText(0, endOffset);
                    // 01 02 03 35  两位1-35之间的数字 空格分隔 0 1 2 3开头
                    if (!text.matches("^(((0[1-9]?)|([12][0-9])|(3[0-5]))(\\s)){0,4}((0[1-9]?)|([12][0-9]?)|(3[0-5]?))?")) {
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
                    } else {  // 格式正确时 去除重复号码
                        final String[] nuArr = text.split(" ");
                        int numLen = nuArr.length;
                        boolean flag = false;
                        for (int i = 0; i < numLen; i++) {
                            for (int j = i + 1; j < numLen; j++) {
                                if (nuArr[i].equals(nuArr[j])) {
                                    flag = true;
                                }
                            }
                        }
                        if (flag) {
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
                        } else {
                            int textLen = text.length();
                            if (textLen % 3 == 2) {
                                if (numLen < 5) {
                                    tipLabel.setText("当前号码" + numLen + "个，请继续输入...");
                                } else if (numLen == 5) {
                                    tipLabel.setText("输入完成");
                                }
                            }
                        }
                    }
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                final Document document = e.getDocument();
                final int endOffset = document.getLength();
                String text = null;
                try {
                    text = document.getText(0, endOffset);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                final String[] nuArr = text.split(" ");
                int numLen = nuArr.length;
                int textLen = text.length();
                if (textLen < 2) {
                    tipLabel.setText("当前号码0个，请继续输入...");
                    return;
                }
                if (textLen % 3 == 1) {
                    tipLabel.setText("当前号码" + (numLen - 1) + "个，请继续输入...");
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("changedUpdate");
            }
        });
        JPanel p4 = new JPanel(new BorderLayout(5, 0));
        p4.setFont(new Font("宋体", Font.PLAIN, 18));
        p4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "输入号码(空格分隔)eg.01 25 11 09 34", TitledBorder.LEFT, TitledBorder.TOP));
        p4.setPreferredSize(new Dimension(300, 180));

        JPanel p5 = new JPanel(new GridLayout(0, 2));
        p5.setPreferredSize(new Dimension(300, 100));
        final JLabel label01 = new JLabel();
        label01.setName("012路比：");
        final JLabel label02 = new JLabel();
        label02.setName("三区比：");
        final JLabel label03 = new JLabel();
        label03.setName("大小比：");
        final JLabel label04 = new JLabel();
        label04.setName("奇偶比：");
        final JLabel label05 = new JLabel();
        label05.setName("质合比：");
        final JLabel label06 = new JLabel();
        label06.setName("ac值：");
        final JLabel label07 = new JLabel();
        label07.setName("首位差值尾：");
        p5.add(label01);
        p5.add(label02);
        p5.add(label03);
        p5.add(label04);
        p5.add(label05);
        p5.add(label06);
        p5.add(label07);

        JPanel p6 = new JPanel();
        p6.setPreferredSize(new Dimension(300, 300));
        final JTextPane textPane = new JTextPane();
        textPane.setPreferredSize(new Dimension(300, 180));
        p6.add(new JScrollPane(textPane));

        JButton button3 = new JButton("测试");
        button3.setPreferredSize(new Dimension(30, 30));
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String testNum = textField.getText();
                if (StringUtils.isBlank(testNum)) {
                    JOptionPane.showMessageDialog(FilterNumberPanel.this, "输入的测试号码有误！");
                    return;
                }
                final String[] nuArr = testNum.split(" ");
                TreeSet<Integer> set = NumberUtil.stringArrToTreeSet(nuArr);
                int numLen = nuArr.length;
                if (numLen != set.size()) {
                    JOptionPane.showMessageDialog(FilterNumberPanel.this, "有重复数字请检查！");
                    return;
                }
                final int[] data = NumberUtil.stringArrToIntArray(nuArr);
                label01.setText(label01.getName() + NumberUtil.getRemainderByThreeCount(data));
                label02.setText(label02.getName() + NumberUtil.areaRatioByThree(data));
                label03.setText(label03.getName() + NumberUtil.getBigNumRatio(data));
                label04.setText(label04.getName() + NumberUtil.getOddNumRatio(data));
                label05.setText(label05.getName() + NumberUtil.getPrimerRatio(data));
                label06.setText(label06.getName() + NumberUtil.ac(data));
                List<Integer> getShouWeiChaNumList = NumberUtil.getShouWeiChaNumList(data);
                TreeSet<Integer> numSet = NumberUtil.intArrayToTreeSet(data);
                numSet.retainAll(getShouWeiChaNumList);
                label07.setText(label07.getName() + numSet);
                clearEditorPanel();
//                StringBuilder stringBuilder = new StringBuilder("");
                insertNum("测试号码：" + textField.getText());
//                stringBuilder.append("测试号码").append(textField.getText()).append(System.getProperty("line.separator"));
                // 首次过滤条件
                Pipeline pipeline = DefaultPipeline.getInstance();
                List<ConditionFilter> list = pipeline.getFilterList();
                for (ConditionFilter f : list) {
                    f.filter(data, true);
                    insertString(f.getInfoList());
//                    stringBuilder.append(buildLabelStr(f.getInfoList()));
                }
                GLog.logger.info("首次检验完成......");
                DynamicConditionFilter dynamicConditionFilter = DynamicConditionFilter.getInstance(ConditionFilterPanel.getInstance().getSelectedCondition());
                dynamicConditionFilter.testFilter(data);
                insertString(dynamicConditionFilter.getInfoList());
//                stringBuilder.append(buildLabelStr(dynamicConditionFilter.getInfoList()));
                GLog.logger.info("动态条件检验完成......");
                PropertyConditionFilter propertyConditionFilter = PropertyConditionPanel.getInstance().buildCondition();
                propertyConditionFilter.testFilter(data);
                insertString(propertyConditionFilter.getInfoList());
//                stringBuilder.append(buildLabelStr(propertyConditionFilter.getInfoList()));
                GLog.logger.info("属性条件检验完成......");
//                textPane.setText(stringBuilder.toString());
            }
        });
        p4.add(textField, BorderLayout.WEST);
        p4.add(button3, BorderLayout.CENTER);
        p4.add(tipLabel, BorderLayout.NORTH);
        p4.add(p5, BorderLayout.SOUTH);
        panel.add(p4);

//        panel.add(p6);

        return panel;
    }

    public void clearEditorPanel() {
        final Document document = editorPane.getDocument();
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private String getEditorPanelContent() {
        final Document document = editorPane.getDocument();
        String text = null;
        try {
            text = document.getText(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return text;
    }

    private void insertNum(String num) {
        final Document document = editorPane.getDocument();
        int end = document.getEndPosition().getOffset();
        final String line = System.getProperty("line.separator");
        try {
            document.insertString(end - 1, num + line, new SimpleAttributeSet(getAttributeSet()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void insertString(final List<String> infoList) {
//        clearEditorPanel();
        final Document document = editorPane.getDocument();
        final String line = System.getProperty("line.separator");
        int start = document.getStartPosition().getOffset();
        int end = document.getEndPosition().getOffset();
        try {
            StringBuilder sb = new StringBuilder("");
            for (String str : infoList) {
                sb.append(str).append(line);
            }
            document.insertString(end - 1, sb.toString(), new SimpleAttributeSet(getAttributeSet()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private String buildLabelStr(final List<String> infoList) {
        final String line = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder("");
        for (String str : infoList) {
            sb.append(str).append(line);
        }
        return sb.toString();
    }

    private AttributeSet getAttributeSet() {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();  //创建一个属性
        StyleConstants.setFontSize(simpleAttributeSet, 20);   //设置字体大小
        StyleConstants.setForeground(simpleAttributeSet, Color.GREEN);    //设置前景色
        StyleConstants.setBackground(simpleAttributeSet, Color.BLUE);
        StyleConstants.setBold(simpleAttributeSet, true);    //设置加粗
        StyleConstants.setUnderline(simpleAttributeSet, true);    //设置下划线
        StyleConstants.setFontFamily(simpleAttributeSet, "微软雅黑");   //设置字体
        StyleConstants.setItalic(simpleAttributeSet, true);   //设置倾斜
        StyleConstants.setStrikeThrough(simpleAttributeSet, true);   //设置删除线
        StyleConstants.setFirstLineIndent(simpleAttributeSet, 2.5f);    //设置首行缩进
        StyleConstants.setLineSpacing(simpleAttributeSet, 20);
        StyleConstants.setRightIndent(simpleAttributeSet, 2.4f);
        return simpleAttributeSet;
    }

    public void saveFile() {
        File file = FileUtil.getSaveFile(this, "文本文件(*.txt)");
        String classpath = NumberUtil.getClassPath();
        byte[] bytes = FileUtil.readFile(new File(classpath + ConfigUtil.getFilterFinalResult()));
        FileUtil.writeFile(bytes, file);
    }

    private JScrollPane filterNumberPanel() {
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(BorderFactory.createTitledBorder(""));
        scrollPane.setPreferredSize(new Dimension(400, 600));
        return scrollPane;
    }
}
