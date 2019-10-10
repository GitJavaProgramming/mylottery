package org.pp.component.list;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class CList<E> extends JList<E> {

    private List<E> data;

    private class DataModel extends AbstractListModel {

        @Override
        public int getSize() {
            return data.size();
        }

        @Override
        public E getElementAt(int index) {
            return data.get(index);
        }
    }

    public CList() {
        setModel(new DataModel());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public CList(List<E> data) {
        this();
        this.data = data;
    }

    public void addElement(E e) {
        data.add(e);
        setListData(data);
    }

    public void removeElement(E e) {
        data.remove(e);
        setListData(data);
    }

    public void removeElementList(List<E> list) {
        data.removeAll(list);
        setListData(data);
    }

    public void setListData(List<E> listData) {
        this.data = listData;
        Vector<E> vector = new Vector<>(listData);
        super.setListData(vector);
    }

    @Override
    public void setListData(E[] listData) {
        super.setListData(listData);
        this.data = Arrays.asList(listData);
    }

    @Override
    public void setListData(Vector<? extends E> listData) {
        super.setListData(listData);
        this.data = new ArrayList<E>(listData);
    }

    public void clearDataAndReset() {
        this.data = new LinkedList<>();
        setListData(data);
    }

    public void selectAll() {
        int len = data.size();
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = i;
        }
        this.setSelectedIndices(array);
    }

    public List<E> getData() {
        return data;
    }


    class CheckRenderer<E> implements ListCellRenderer<E> {
        JCheckBox check = new JCheckBox();

        public Component getListCellRendererComponent(final JList<? extends E> list, E value, final int index, final boolean isSelected, boolean cellHasFocus) {

            check.setSelected(isSelected);
            check.setText(value.toString());
            check.setFont(list.getFont());
            check.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JCheckBox c = (JCheckBox) e.getSource();
//                    System.out.println(c.getText());
                    if (isSelected) {
                        list.setSelectedIndex(index);
                    } else {
                        list.removeSelectionInterval(index, index);
                    }
                }
            });
            return check;
        }
    }

    class LabelRenderer<E> implements ListCellRenderer<E> {
        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(this.getBackground());
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                super.paintComponent(g);
            }

        };

        public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
            label.setPreferredSize(new Dimension(100, 24));
            label.setText("  " + value.toString());
            label.setFont(list.getFont());
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
            } else {
                label.setBackground(list.getBackground());
            }
            label.repaint(100);
            return label;
        }
    }

    class RadioRenderer<E> implements ListCellRenderer<E> {
        JRadioButton radio = new JRadioButton();

        public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
            radio.setSelected(isSelected);
            radio.setText(value.toString());
            radio.setFont(list.getFont());
//            if (isSelected) {
//                radio.setBackground(list.getSelectionBackground());
//            } else {
//                radio.setBackground(list.getBackground());
//            }
            return radio;
        }
    }

    @Override
    public void setSelectionMode(int selectionMode) {
        if (selectionMode == ListSelectionModel.SINGLE_SELECTION) { // 一次只能选择一个项目
            this.setCellRenderer(new LabelRenderer());
            this.setSelectionModel(createSelectionModel());
        } else if (selectionMode == ListSelectionModel.SINGLE_INTERVAL_SELECTION) { // 连续项目选择，按住shift键连续选择
            this.setCellRenderer(new RadioRenderer());
            this.setSelectionModel(createSelectionModel());
        } else if (selectionMode == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {  // 默认模式，包含单一选择，和连续项目选择，不连续项目按住ctrl键选择
            this.setCellRenderer(new CheckRenderer());
            // 设置选择模型  任意多选
            this.setSelectionModel(new DefaultListSelectionModel() {
                @Override
                public void setSelectionInterval(int index0, int index1) {
                    if (isSelectedIndex(index0)) {
                        removeSelectionInterval(index0, index1);
                    } else {
                        addSelectionInterval(index0, index1);
                    }
                }
            });
        }
    }
}
