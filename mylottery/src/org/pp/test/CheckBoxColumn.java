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
 * Time: 下午7:53
 * To change this template use File | Settings | File Templates.
 */
public class CheckBoxColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    private JCheckBox chkRender;
    private JCheckBox chkEditor;
    private Object value;

    public CheckBoxColumn(TableColumn tc) {
        chkRender = new JCheckBox();
        chkEditor = new JCheckBox();
        chkEditor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
        tc.setCellEditor(this);
        tc.setCellRenderer(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        chkRender.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        chkRender.setSelected((Boolean) value);
        return chkRender;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        chkEditor.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        if (value != null) {
            Boolean res = (Boolean) value;
            res = !res;
            this.value = res;
            chkEditor.setSelected(res);
        }
        return chkEditor;
    }

    @Override
    public Object getCellEditorValue() {
        return this.value;
    }

}