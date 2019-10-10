package org.pp.test;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-29
 * Time: 下午7:54
 * To change this template use File | Settings | File Templates.
 */
public class ButtonColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, ActionListener {
    private JButton btnRender;
    private JButton btnEditor;
    private Object value;
//    private ManagerCenter center;

//    public ButtonColumn(TableColumn tc, String name, ManagerCenter center) {
//        this.center = center;
//        btnRender = new JButton(name);
//        btnEditor = new JButton(name);
//        btnEditor.addActionListener(this);
//        tc.setCellEditor(this);
//        tc.setCellRenderer(this);
//    }

    @Override
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return this.value;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus) {
            btnRender.setForeground(table.getForeground());
            btnRender.setBackground(UIManager.getColor("Button.background "));
        } else if (isSelected) {
            btnRender.setForeground(table.getSelectionForeground());
            btnRender.setBackground(table.getSelectionBackground());
        } else {
            btnRender.setForeground(table.getForeground());
            btnRender.setBackground(UIManager.getColor("Button.background "));
        }
        return btnRender;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // TODO Auto-generated method stub
        this.value = value;
        if (isSelected) {
            btnEditor.setForeground(table.getSelectionForeground());
            btnEditor.setBackground(table.getSelectionBackground());
        } else {
            btnEditor.setForeground(table.getForeground());
            btnEditor.setBackground(UIManager.getColor("Button.background "));
        }
        return btnEditor;
    }

    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // System.out.println(value);
        fireEditingStopped();
//        new BookDialog(center, Integer.parseInt(value.toString()));
    }
}