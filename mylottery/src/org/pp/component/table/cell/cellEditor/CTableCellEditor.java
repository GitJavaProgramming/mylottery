package org.pp.component.table.cell.cellEditor;

import org.pp.component.table.cell.cellCombo.CCellComboBox;
import org.pp.component.table.cell.cellComponent.CTextCellComponent;
import org.pp.component.table.cell.cellComponent.ITableCellComponent;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class CTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    protected ITableCellComponent component;
    protected int clickCountToStart = 1;

    public CTableCellEditor() {
        this(new CTextCellComponent()); // 默认显示文本
    }

    public CTableCellEditor(ITableCellComponent tableCellComponent) {
        this.component = tableCellComponent;
    }

    public CTableCellEditor setShowInCombo(JTable table) {
        component = new CCellComboBox(component, table);
        CCellComboBox cCellComboBox = (CCellComboBox) component;
//        cCellComboBox.addActionListener(this);
        return this;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return component.getValue();
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        setValue(value);
        return component.getComponent();
    }

    public Object getValue() {
        return component.getValue();
    }

    public JComponent getComponent() {
        return component.getComponent();
    }

    public void setValue(Object value) {
        component.setValue(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            stopCellEditing();
        } catch (Exception ex) {
            cancelCellEditing();
        }
    }
}
