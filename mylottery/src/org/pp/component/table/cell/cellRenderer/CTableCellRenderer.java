package org.pp.component.table.cell.cellRenderer;

import org.pp.component.table.CGridTable;
import org.pp.component.table.ColumnInfo;
import org.pp.component.table.PropSetting;
import org.pp.component.table.cell.cellComponent.ITableCellComponent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * 单元格渲染器，java没有提供默认的表头渲染器 javax.swing.table包里面找不到 sun.swing.table.DefaultTableCellHeaderRenderer 可能出现变动不推荐使用
 */
public class CTableCellRenderer extends DefaultTableCellRenderer {

    protected ITableCellComponent component;
    protected boolean showComponent = false;

    public CTableCellRenderer() {
        super();
    }

    public CTableCellRenderer(ITableCellComponent component) {
        this.component = component;
    }

    public CTableCellRenderer setShowComponent(boolean showComponent) {
        this.showComponent = showComponent;
        return this;
    }

    public boolean isOpaque() {
        return getComponent() != null && getComponent().getComponent() != null && isShowComponent() ?
                getComponent().getComponent().isOpaque() : super.isOpaque();
    }

    @Override
    public void setForeground(Color c) {
        if (getComponent() != null && getComponent().getComponent() != null) {
            getComponent().getComponent().setForeground(c);
        }
        super.setForeground(c);
    }

    @Override
    public void setBackground(Color c) {
        if (getComponent() != null && getComponent().getComponent() != null) {
            getComponent().getComponent().setBackground(c);
        }
        super.setBackground(c);
    }

    @Override
    public void updateUI() {
        if (getComponent() != null && getComponent().getComponent() != null)
            getComponent().getComponent().updateUI();
        super.updateUI();
    }

    public void setValue(Object value) {
        if (getComponent() != null)
            getComponent().setValue(value);
        super.setValue(value);
    }

    public Object getValue() {
        return component.getValue();
    }

    public String getTextByValue(Object value) {
        return component.getTextByValue(value);
    }

    public ITableCellComponent getComponent() {
        return component;
    }

    public boolean isShowComponent() {
        return showComponent;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (component == null || !(component instanceof ITableCellComponent)) {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        if (isShowComponent() && component.getComponent() != null) {
            try {
                component.setValue(value);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (isSelected) {
                component.getComponent().setOpaque(true);
            } else {
                component.getComponent().setOpaque(false);
            }
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());

            return component.getComponent();
        } else {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(component.getTextByValue(value));
            return this;
        }
    }

    public <T> void prepareGetTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JComponent c = component.getComponent();
        CGridTable<T> gridTable = ((CGridTable<T>) table);
        ColumnInfo[] columns = gridTable.getVisibleColumns();
        ColumnInfo columnInfo = columns[column % columns.length];
        if (columnInfo.isSelectColumn()) { // 去除选中行的选择列的背景
            c.setBackground(null);
            return;
        }
        PropSetting columnPropSetting = columns[column].getPropSetting();
        PropSetting<T> rowPropSetting = gridTable.getRowPropSetting();
        boolean columnSetPropFirst = gridTable.isColumnSetPropFirst();
        if (rowPropSetting != null || columnPropSetting != null) {
            boolean selected = gridTable.isCellSelected(row, column);
            Color fg = null;
            Color bg = null;
            Font f = null;
            T entry = gridTable.getEntry(row);
            PropSetting<T> first, second;
            if (columnSetPropFirst) { // 列优先
                first = columnPropSetting;
                second = rowPropSetting;
            } else { // 行优先设置
                first = rowPropSetting;
                second = columnPropSetting;
            }
            // 默认行优先
            if (second != null) {
                bg = second.getBackground(entry, selected);
                fg = second.getForeground(entry, selected);
                f = second.getFont(entry, c.getFont(), selected);
            }
            if (first != null) {
                if (bg == null) {
                    bg = first.getBackground(entry, selected);
                }
                if (fg == null) {
                    fg = first.getForeground(entry, selected);
                }
                if (f == null) {
                    f = first.getFont(entry, c.getFont(), selected);
                }
            }

            if (c != null) {
                c.setBackground(bg != null ? bg : (selected ? gridTable.getSelectionBackground() : getBackground()));
                c.setForeground(fg != null ? fg : (selected ? gridTable.getSelectionForeground() : getForeground()));
            }

            if (c != null && f != null) {
                c.setFont(f);
            }
        }
    }

    public void setComponent(ITableCellComponent component) {
        this.component = component;
    }
}
