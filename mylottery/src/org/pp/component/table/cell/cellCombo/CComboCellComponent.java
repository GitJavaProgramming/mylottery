package org.pp.component.table.cell.cellCombo;

import org.pp.component.CCombo;
import org.pp.component.table.cell.cellComponent.ITableCellComponent;
import org.pp.component.table.cell.cellEditor.CTableCellEditor;
import org.pp.component.table.cell.cellRenderer.CTableCellRenderer;
import org.pp.util.ClassUtil;

import javax.swing.*;
import java.util.List;

public class CComboCellComponent extends CCombo implements ITableCellComponent {

    public static CTableCellEditor createEditor(List<NameValue> items) {
        return new CTableCellEditor(new CComboCellComponent(items));
    }

    public static CTableCellEditor createEditor(boolean nullable, List<NameValue> items) {
        return new CTableCellEditor(new CComboCellComponent(nullable, items));
    }

    public static CTableCellRenderer createRenderer(List<NameValue> items) {
        return new CTableCellRenderer(new CComboCellComponent(items));
    }

    public static CTableCellEditor createEditor(NameValue... items) {
        return new CTableCellEditor(new CComboCellComponent(items));
    }

    public static CTableCellRenderer createRenderer(NameValue... items) {
        return new CTableCellRenderer(new CComboCellComponent(items));
    }

    public CComboCellComponent() {
        super();
    }

    public CComboCellComponent(boolean nullable) {
        super(nullable);
    }

    public CComboCellComponent(NameValue... items) {
        super(items);
    }

    public CComboCellComponent(boolean nullable, NameValue... items) {
        super(nullable, items);
    }

    public CComboCellComponent(List<NameValue> items) {
        super(items);
    }

    public CComboCellComponent(boolean nullable, List<NameValue> items) {
        super(nullable, items);
    }

    @Override
    public String getTextByValue(Object value) {
        int i = getIndexOfValue(value);

        return i < 0 ? (value == null ? "" : value.toString()) : ((NameValue) getItemAt(i)).getName();
    }

    public int getIndexOfValue(Object value) {
        for (int i = 0; i < getItemCount(); ++i) {
            if (ClassUtil.safeEquals(((NameValue) getItemAt(i)).getValue(), value))
                return i;
        }
        return -1;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void setValue(Object value) {
        oldValue = value;
        for (int i = 0; i < getItemCount(); ++i) {
            if (ClassUtil.safeEquals(((NameValue) getItemAt(i)).getValue(), value)) {
                setSelectedIndex(i);
                return;
            }
        }
        setSelectedItem(isEditable() && value instanceof String ? value : null);
    }

    @Override
    public Object getValue() {
        if (isEditable() && getEditor().getItem() instanceof String)
            return getEditor().getItem();
        return getSelectedIndex() == -1 ? oldValue : ((NameValue) getSelectedItem()).getValue();
    }

    protected Object oldValue;
}
