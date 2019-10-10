package org.pp.panel;

import org.apache.commons.lang3.StringUtils;
import org.pp.component.table.IFilterComponent;
import org.pp.component.table.cell.cellComponent.ITableCellComponent;
import org.pp.util.NumberUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class CPanelCellComponent /*extends ConditionFilterPanel*/ implements ITableCellComponent, IFilterComponent {

    private JPanel panel;
    protected final Set<String> conditionBuilder = new TreeSet<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
        }
    });
    protected final TreeSet<Integer> numberCountBuilder = new TreeSet<>();

    protected final CopyOnWriteArraySet<JCheckBox> checkBoxSet = new CopyOnWriteArraySet<>();

    public CPanelCellComponent() {
    }

    public CPanelCellComponent(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getTextByValue(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public JComponent getComponent() {
        if (panel == null) {
            panel = initCardP3("七行五列视图");
        }
        return panel;
    }

    @Override
    public Object getValue() {
        if (conditionBuilder.isEmpty()) {
//            System.out.println("1" + conditionBuilder.toString().replaceAll("\\[| |]", "") + "1");
            return defaultValue;
        }
        return conditionBuilder.toString().replaceAll("\\[| |]", "");
    }

    @Override
    public void setValue(Object value) {
        cleanPanel();
        String values = value.toString();
        String[] array = values.split(",");
        Set<String> set = new HashSet<>();
        for (String str : array) {
            set.add(str);
        }
//        Set<Integer> set1 = NumberUtil.stringArrToTreeSet(array);
        java.util.List<JCheckBox> components = getAllJCheckBox(panel);
        for (JCheckBox checkBox : components) {
            String text = checkBox.getText();
            if (set.contains(text)) {
                checkBox.setSelected(true);
            }
        }
//        ((JCheckBox) ((JPanel) panel.getComponents()[0]).getComponents()[1]).isSelected();
//        panel.repaint();
    }

    private java.util.List<JCheckBox> getAllJCheckBox(Component component) {
        java.util.List<JCheckBox> result = new ArrayList<>();
        if (component instanceof JPanel) {
            for (Component com : ((JPanel) component).getComponents()) {
                java.util.List<JCheckBox> textFields = getAllJCheckBox(com);
                result.addAll(textFields);
            }
        } else {
            if (component instanceof JCheckBox) {
                result.add((JCheckBox) component);
            }
        }
        return result;
    }

    protected JPanel initCardP3(String name) {
        JPanel panel = new JPanel();
        panel.setName(name);
        panel.setLayout(new GridLayout(7, 0));
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

        return panel;
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

    protected void cleanPanel() {
        for (JCheckBox checkBox : checkBoxSet) {
            checkBox.setSelected(false);
        }
        conditionBuilder.clear();
        numberCountBuilder.clear();
    }

    public CopyOnWriteArraySet<JCheckBox> getCheckBoxSet() {
        return checkBoxSet;
    }

    public Set<String> getConditionBuilder() {
        return conditionBuilder;
    }

    public TreeSet<Integer> getNumberCountBuilder() {
        return numberCountBuilder;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void filter(DocumentEvent event) {
        final Document document = event.getDocument();
        String text = null;
        try {
            text = document.getText(0, document.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        if (text.matches("^(((0[1-9]?)|([12][0-9])|(3[0-5])),){0,35}((0[1-9]?)|([12][0-9]?)|(3[0-5]?))?")) {
            final String[] nuArr = text.split(",");
            TreeSet<Integer> set = NumberUtil.stringArrToTreeSet(nuArr);
            int numLen = nuArr.length;
            if (numLen != set.size()) {
                final String finalText1 = text;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            document.remove(finalText1.length() - 1, 1);
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "输入号码重复。");
                    }
                });
            } else {
                if (!StringUtils.isBlank(text)) {
                    setValue(text);
                } else {
                    setValue(defaultValue);
                }
            }
        } else {
            final String finalText = text;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        document.remove(finalText.length() - 1, 1);
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public boolean toFilter() {
        return true;
    }

    private Object defaultValue = "01";

    public Object getDefaultValue() {
        return defaultValue;
    }
}
