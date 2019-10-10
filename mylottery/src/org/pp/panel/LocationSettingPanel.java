package org.pp.panel;

import org.pp.filter.LocationFilter;
import org.pp.model.Pair;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationSettingPanel extends CPanel {

    private LocationFilter filter = new LocationFilter();

    private static class InstanceHolder {
        private static LocationSettingPanel instance = new LocationSettingPanel();
    }

    private LocationSettingPanel() {
        setPreferredSize(new Dimension(450, 410));
        setBorder(new EmptyBorder(20, 5, 5, 5));
        setLayout(new GridLayout(1, 5));

        add(locationPanel("第一位", 1));
        add(locationPanel("第二位", 2));
        add(locationPanel("第三位", 3));
        add(locationPanel("第四位", 4));
        add(locationPanel("第五位", 5));
    }

    public static LocationSettingPanel getInstance() {
        return InstanceHolder.instance;
    }

    private JPanel locationPanel(String name, int index) {
        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(0,1));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createDashedBorder(SystemColor.desktop), name, TitledBorder.CENTER, TitledBorder.TOP));

        panel.add(jiOuPanel(index));
        panel.add(primerPanel(index));
        panel.add(chuSanYuShuPanel(index));
        return panel;
    }

    private JPanel jiOuPanel(int index) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "奇偶属性", TitledBorder.LEFT, TitledBorder.TOP));
//        ButtonGroup grp = new ButtonGroup();
        JCheckBox checkBox1 = new JCheckBox("奇数");
        JCheckBox checkBox2 = new JCheckBox("偶数");
//        grp.add(checkBox1);
//        grp.add(checkBox2);
        Pair pair = new Pair("奇偶属性", index);
        Map<Pair, List<String>> pairMap = filter.getPairMap();
        pairMap.put(pair, new ArrayList<String>());
        checkBox1.addItemListener(new ItemListenerHandler(pair, filter));
        checkBox2.addItemListener(new ItemListenerHandler(pair, filter));

        panel.add(checkBox1);
        panel.add(checkBox2);
        return panel;
    }

    private class ItemListenerHandler implements ItemListener {

        private final Pair pair;
        private final LocationFilter filter;

        private ItemListenerHandler(Pair pair, LocationFilter filter) {
            this.pair = pair;
            this.filter = filter;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            Map<Pair, List<String>> pairMap = filter.getPairMap();
            List<String> values = pairMap.get(pair);
            JCheckBox c = (JCheckBox) e.getSource();
            String value = c.getText();
            if (c.isSelected()) {
                values.add(value);
            } else {
                values.remove(value);
            }
        }
    }

    private JPanel primerPanel(int index) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "质合属性", TitledBorder.LEFT, TitledBorder.TOP));
//        ButtonGroup grp = new ButtonGroup();
        JCheckBox checkBox1 = new JCheckBox("质数");
        JCheckBox checkBox2 = new JCheckBox("合数");
//        grp.add(checkBox1);
//        grp.add(checkBox2);
        Pair pair = new Pair("质合属性", index);
        Map<Pair, List<String>> pairMap = filter.getPairMap();
        pairMap.put(pair, new ArrayList<String>());
        checkBox1.addItemListener(new ItemListenerHandler(pair, filter));
        checkBox2.addItemListener(new ItemListenerHandler(pair, filter));

        panel.add(checkBox1);
        panel.add(checkBox2);
        return panel;
    }

    private JPanel chuSanYuShuPanel(int index) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "除3余数", TitledBorder.LEFT, TitledBorder.TOP));
//        ButtonGroup grp = new ButtonGroup();
        JCheckBox checkBox1 = new JCheckBox("0");
        JCheckBox checkBox2 = new JCheckBox("1");
        JCheckBox checkBox3 = new JCheckBox("2");
//        grp.add(checkBox1);
//        grp.add(checkBox2);
//        grp.add(checkBox3);
        Pair pair = new Pair("除3余数", index);
        Map<Pair, List<String>> pairMap = filter.getPairMap();
        pairMap.put(pair, new ArrayList<String>());
        checkBox1.addItemListener(new ItemListenerHandler(pair, filter));
        checkBox2.addItemListener(new ItemListenerHandler(pair, filter));
        checkBox3.addItemListener(new ItemListenerHandler(pair, filter));
        panel.add(checkBox1);
        panel.add(checkBox2);
        panel.add(checkBox3);
        return panel;
    }

    public LocationFilter buildFilter() {
        return filter;
    }
}
