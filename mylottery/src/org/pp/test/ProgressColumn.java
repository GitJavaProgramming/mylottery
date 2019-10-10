package org.pp.test;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-29
 * Time: 下午7:56
 * To change this template use File | Settings | File Templates.
 */
public class ProgressColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private JProgressBar pbRender;

    public ProgressColumn(TableColumn tc, int maximum) {
        pbRender = new JProgressBar();
        pbRender.setMaximum(maximum);
        pbRender.setBorder(null);
        tc.setCellEditor(this);
        tc.setCellRenderer(this);
    }

    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        pbRender.setString(value.toString());
        pbRender.setStringPainted(true);
        pbRender.setValue(Integer.parseInt(value.toString()));
        return pbRender;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return null;
    }
}