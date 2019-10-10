package org.pp.panel;

import org.pp.component.list.CList;
import org.pp.component.table.CGridTable;
import org.pp.component.table.CellAlignEnum;
import org.pp.component.table.ColumnInfo;
import org.pp.component.table.cell.cellCombo.CCellComboBox;
import org.pp.component.table.cell.cellCombo.CListCellComponent;
import org.pp.component.table.cell.cellEditor.CTableCellEditor;
import org.pp.model.ConditionRowData;
import org.pp.util.NumberUtil;
import practice.util.DataUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-28
 * Time: 下午2:54
 * To change this template use File | Settings | File Templates.
 */
public class ConditionFilterPanel extends CPanel {

    private static final ConditionFilterPanel instance = new ConditionFilterPanel();

    protected final Set<String> conditionBuilder = new TreeSet<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
        }
    });
    protected final TreeSet<Integer> numberCountBuilder = new TreeSet<>();

    protected final CopyOnWriteArraySet<JCheckBox> checkBoxSet = new CopyOnWriteArraySet<>();

    private CList<String> cList;

    private CGridTable<ConditionRowData> gridTable;

    public ConditionFilterPanel() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(getWidth(), getHeight()));
        initPanel();
    }

    protected void initPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        this.add(panel);

        this.add(buildConditionPanel(), BorderLayout.WEST);
        this.add(conditionPanel(), BorderLayout.CENTER);
    }

    private JPanel buildConditionPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(450, getHeight() - 5));

        JPanel cardPanel = initP0();
        cardPanel.setPreferredSize(new Dimension(450, 310));
        panel.add(cardPanel);

        panel.add(initP2());

        panel.add(buildCenterPanel());

        return panel;
    }

    protected JPanel initP2() {
        JPanel panel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);

//        GridBagConstraints constraints2 = new GridBagConstraints(1, 0/*左上角位置*/, 1, 1/*所占行数和列数*/, 1, 1/*x和y方向上的增量*/, GridBagConstraints.CENTER/*对齐方式*/, GridBagConstraints.HORIZONTAL/*是否拉伸及拉伸方向*/, new Insets(0, 0, 0, 0)/*外部填充：上左下右*/, 0, 0/*内部填充*/);
        JPanel p1 = new JPanel();
        p1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "选号出现个数", TitledBorder.CENTER, TitledBorder.TOP));
        for (int i = 0; i <= 5; i++) {
            JCheckBox checkBox = new JCheckBox("" + i);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox c = (JCheckBox) e.getSource();
                    String text = c.getText();
                    Integer x = DataUtils.asInt(text);
                    if (c.isSelected()) {
                        checkBoxSet.add(c);
                        numberCountBuilder.add(x);
                    } else {
                        if (numberCountBuilder.contains(x)) {
                            checkBoxSet.remove(c);
                            numberCountBuilder.remove(x);
                        }
                    }
                }
            });
            p1.add(checkBox);
        }
//        gridBagLayout.setConstraints(p1, constraints2);
        panel.add(p1);

        return panel;
    }

    protected JPanel initP0() {
        final JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout());
        cardPanel.setToolTipText("右键弹出菜单切换视图");

        final JPopupMenu popupMenu = new JPopupMenu(/*"切换视图"*/);
        popupMenu.add(new JMenuItem("六行六列视图"));
        popupMenu.addSeparator();
        popupMenu.add(new JMenuItem("五行七列视图"));
        popupMenu.add(new JMenuItem("七行五列视图"));
        popupMenu.add(new JMenuItem("十行视图"));
        cardPanel.setComponentPopupMenu(popupMenu);
        MenuElement[] menuElements = popupMenu.getSubElements();
        for (MenuElement menuElement : menuElements) {
            menuElement.getComponent().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JMenuItem menuItem = (JMenuItem) e.getComponent();
                    String showCard = menuItem.getText();
                    CardLayout layout = (CardLayout) (cardPanel.getLayout());
                    Component[] components = cardPanel.getComponents();
                    for (Component c : components) {
                        if (c.isVisible() && !showCard.equals(c.getName())) { // 当前显示的和将要显示的不是同一个就显示
                            cleanPanel();
                            layout.show(cardPanel, showCard);
                        }
                    }
                    popupMenu.setVisible(false);
                }
            });
        }

        cardPanel.add("六行六列视图", initCardP1("六行六列视图"));
        cardPanel.add("五行七列视图", initCardP2("五行七列视图"));
        cardPanel.add("七行五列视图", initCardP3("七行五列视图"));
        cardPanel.add("十行视图", initCardP4("十行视图"));

        return cardPanel;
    }

    protected JPanel initCardP4(String name) {
        // 卡片布局 每个卡片大小相等
//        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel panel = new JPanel();
        panel.setName(name);
        panel.setLayout(new GridLayout(10, 1));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), name, TitledBorder.CENTER, TitledBorder.TOP));


        for (int j = 0; j <= 9; j++) {
            final JPanel panel0 = buildPanel0();
            for (int i = 1; i <= 35; i++) {
                if (i % 10 == j) {
                    JCheckBox checkBox = new JCheckBox(NumberUtil.format(i));
                    panel0.add(checkBox);
                    checkBox.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            JCheckBox c = (JCheckBox) e.getSource();
                            String text = c.getText();
                            if (c.isSelected()) {
                                checkBoxSet.add(c);
                                conditionBuilder.add(text);
                            } else {
                                if (conditionBuilder.contains(text)) {
                                    checkBoxSet.remove(c);
                                    conditionBuilder.remove(text);
                                }
                            }
                        }
                    });
                }
            }
            panel.add(panel0);
        }

//        panel.setPreferredSize(new Dimension(300, 310));
//        scrollPane.setViewportView(panel);

        return panel;
    }

    protected JPanel initCardP3(String name) {
        JPanel panel = new JPanel();
        panel.setName(name);
        panel.setLayout(new GridLayout(10, 1));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), name, TitledBorder.CENTER, TitledBorder.TOP));

        for (int j = 1; j <= 7; j++) {
            final JPanel panel0 = buildPanel0();
            for (int i = 1; i <= 35; i++) {
                if ((i <= 5 * (j - 1) + 5) && (i > 5 * (j - 1))) {
                    JCheckBox checkBox = new JCheckBox(NumberUtil.format(i));
                    checkBox.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            JCheckBox c = (JCheckBox) e.getSource();
                            String text = c.getText();
                            if (c.isSelected()) {
                                checkBoxSet.add(c);
                                conditionBuilder.add(text);
                            } else {
                                if (conditionBuilder.contains(text)) {
                                    checkBoxSet.remove(c);
                                    conditionBuilder.remove(text);
                                }
                            }
                        }
                    });
                    panel0.add(checkBox);
                }
            }
            panel.add(panel0);
        }
        fillPanel(panel, 3);
        return panel;
    }

    protected JPanel initCardP2(String name) {
        JPanel panel = new JPanel();
        panel.setName(name);
        panel.setLayout(new GridLayout(10, 1));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), name, TitledBorder.CENTER, TitledBorder.TOP));

        for (int j = 1; j <= 5; j++) {
            JPanel panel0 = buildPanel0();
            for (int i = 1; i <= 35; i++) {
                if ((i <= 7 * (j - 1) + 7) && (i > 7 * (j - 1))) {
                    JCheckBox checkBox = new JCheckBox(NumberUtil.format(i));
//                    checkBox.setPreferredSize(new Dimension(50, 20));
                    checkBox.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            JCheckBox c = (JCheckBox) e.getSource();
                            String text = c.getText();
                            if (c.isSelected()) {
                                checkBoxSet.add(c);
                                conditionBuilder.add(text);
                            } else {
                                if (conditionBuilder.contains(text)) {
                                    checkBoxSet.remove(c);
                                    conditionBuilder.remove(text);
                                }
                            }
                        }
                    });
                    panel0.add(checkBox);
                }
            }
            panel.add(panel0);
        }

        fillPanel(panel, 5);

        return panel;
    }

    protected JPanel initCardP1(String name) {
//        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel panel = new JPanel();
        panel.setName(name);
        panel.setLayout(new GridLayout(10, 1));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), name, TitledBorder.CENTER, TitledBorder.TOP));

        for (int j = 1; j <= 6; j++) {
            JPanel panel0 = buildPanel0();
            for (int i = 1; i <= 35; i++) {
                if ((i <= 6 * (j - 1) + 6) && (i > 6 * (j - 1))) {
                    JCheckBox checkBox = new JCheckBox(NumberUtil.format(i));
                    checkBox.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            JCheckBox c = (JCheckBox) e.getSource();
                            String text = c.getText();
                            if (c.isSelected()) {
                                checkBoxSet.add(c);
                                conditionBuilder.add(text);
                            } else {
                                if (conditionBuilder.contains(text)) {
                                    checkBoxSet.remove(c);
                                    conditionBuilder.remove(text);
                                }
                            }
                        }
                    });
                    panel0.add(checkBox);
                }
            }
            panel.add(panel0);
        }

        fillPanel(panel, 4);

//        scrollPane.setViewportView(panel);
//        scrollPane.setPreferredSize(new Dimension(100, 100));
//        panel.setPreferredSize(new Dimension(100, 100));
//        panel.revalidate();
        return panel;
    }

    private void fillPanel(JPanel panel, int count) {
        for (int i = 0; i < count; i++) {
            panel.add(new JPanel());
        }
    }

    protected JPanel buildPanel0() {
        final JPanel panel0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox checkBox0 = new JCheckBox();
        checkBox0.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox c = (JCheckBox) e.getSource();
                boolean flag = c.isSelected();
                if (flag) {
                    checkBoxSet.add(c);
                } else {
                    checkBoxSet.remove(c);
                }
                Component[] components = panel0.getComponents();
                for (Component c1 : components) {
                    if (c1 instanceof JCheckBox) {
                        ((JCheckBox) c1).setSelected(flag);
                    }
                }
            }
        });
        panel0.add(checkBox0);
        return panel0;
    }

    public JPanel buildCenterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(450, 260));

        JButton button = new JButton("清除已选择的条件");
        panel.add(button);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                cleanPanel();
            }
        });

        JButton button1 = new JButton("添加条件");
        button1.addActionListener(new AddActionHandler());
        panel.add(button1);

        JButton button4 = new JButton("删除选中的条件");
        panel.add(button4);
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridTable.deleteEntry();
            }
        });

        JButton button2 = new JButton("从本地导入条件");
        panel.add(button2);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importFile();
            }
        });

        JButton button3 = new JButton("清空表格");
        panel.add(button3);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridTable.clear();
            }
        });

        JButton button5 = new JButton("反选");
        panel.add(button5);
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridTable.inverseCheckedRows();
            }
        });

        JButton button6 = new JButton("导出excel");
        panel.add(button6);
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridTable.exportExcel();
            }
        });

        return panel;
    }

    public void importFile() {
        //弹出文件选择框
        JFileChooser chooser = new JFileChooser();

        //下面的方法将阻塞，直到【用户按下保存按钮且“文件名”文本框不为空】或【用户按下取消按钮】
        int option = chooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {    //假如用户选择了保存
            File file = chooser.getSelectedFile();
            java.util.List<ConditionRowData> rowDataList = new ArrayList<ConditionRowData>(100);
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader);
                String line = null;
                //按行读，并把每次读取的结果保存在line字符串中
                while ((line = reader.readLine()) != null) {
                    if (line.matches("\\s*") || line.startsWith("#")) {
                        continue;
                    }
                    if (!line.matches("^((\\d+),)*(\\d+)-((\\d+),)*(\\d+)$")) {
                        JOptionPane.showMessageDialog(ConditionFilterPanel.this, "导入文件格式错误。");
                        break;
                    }
                    String condition = line.replaceAll("\\[| |]", "");
                    String[] splitCondition = condition.split("-");
                    String s1 = splitCondition[0];
                    s1 = NumberUtil.treeSetToString(NumberUtil.stringArrToTreeSet(s1.split(","))).replaceAll("\\[| |]", "");
                    rowDataList.add(new ConditionRowData(s1, splitCondition[1]));
                }
                reader.close();
                gridTable.addEntryList(rowDataList);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class AddActionHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (conditionBuilder.isEmpty()) {
//                JOptionPane.showMessageDialog(ConditionFilterPanel.this, "请选择号码。");
                return;
            }
            if (numberCountBuilder.isEmpty()) {
//                JOptionPane.showMessageDialog(ConditionFilterPanel.this, "请选择号码出现个数。");
                return;
            }
            int numCount = conditionBuilder.size();
            int max = numberCountBuilder.last();
            if (numCount < max) {
                return;
            }
            String conditionNumber = conditionBuilder.toString().replaceAll("\\[| |]", "");
            String numberCount = numberCountBuilder.toString().replaceAll("\\[| |]", "");
//            String condition = conditionNumber + "-" + numberCount;
//            cList.addElement(condition);
            ConditionRowData rowData = new ConditionRowData(conditionNumber, numberCount);
            gridTable.addEntry(rowData);

            cleanPanel();
        }
    }

    protected void cleanPanel() {
        for (JCheckBox checkBox : checkBoxSet) {
            checkBox.setSelected(false);
        }
        conditionBuilder.clear();
        numberCountBuilder.clear();
    }

    private CList<String> initCList() {
        if (cList == null) {
            cList = new CList<String>();
        }
        return cList;
    }

    private CGridTable<ConditionRowData> initGridTable() {
        if (gridTable == null) {
            gridTable = new CGridTable<>(ColumnInfo.createSelectColumn(), ColumnInfo.createIndexColumn());

            ColumnInfo c1 = new ColumnInfo("selected", "选择的号码").setAlign(CellAlignEnum.center)/*.setEditable(true)*/;
            c1.setEditor(new CTableCellEditor(new CPanelCellComponent()).setShowInCombo(gridTable));
//            c1.setEditor(new CTableCellEditor(new CCellComboBox(new CPanelCellComponent(), gridTable)));

            ColumnInfo c2 = new ColumnInfo("limit", "出现的个数").setAlign(CellAlignEnum.center)/*.setEditable(true)*/.setWidthFixed(true);
            java.util.List<String> countList = new ArrayList(6);
            countList.add("0");
            countList.add("1");
            countList.add("2");
            countList.add("3");
            countList.add("4");
            countList.add("5");
//            c2.setEditor(new CTableCellEditor(new CListCellComponent(countList)).setShowInCombo(gridTable));
            c2.setEditor(new CTableCellEditor(new CCellComboBox(new CListCellComponent(countList), gridTable)));
            gridTable.addColumn(c1, 200);
            gridTable.addColumn(c2, 100);
        }
        return gridTable;
    }

    private JPanel conditionPanel() {
        JPanel panel = new JPanel();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(450, getHeight() - 5));
        scrollPane.getViewport().setView(initGridTable());

        panel.add(scrollPane);
        return panel;
    }

    public java.util.List<String> getAllInputCondition() {
        return cList.getData();
    }

    public java.util.List<String> getSelectedCondition() {
        java.util.List<ConditionRowData> conditionRowDataList = gridTable.getCheckedRows();
        int len = conditionRowDataList.size();
        java.util.List<String> result = new ArrayList(len);
        for (int i = 0; i < len; i++) {
            result.add(conditionRowDataList.get(i).toString());
        }
        return result;
    }

    protected CopyOnWriteArraySet<JCheckBox> getCheckBoxSet() {
        return checkBoxSet;
    }

    public TreeSet<Integer> getNumberCountBuilder() {
        return numberCountBuilder;
    }

    public Set<String> getConditionBuilder() {
        return conditionBuilder;
    }

    public static ConditionFilterPanel getInstance() {
        return instance;
    }
}
