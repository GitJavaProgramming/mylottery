package org.pp.component.table.cell.cellComponent;

import org.pp.component.table.cell.cellEditor.CTableCellEditor;
import org.pp.component.table.cell.cellRenderer.CTableCellRenderer;
import org.pp.util.ClassUtil;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-29
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public class CBooleanCellComponent implements ITableCellComponent {

    private JCheckBox checkBox = new JCheckBox();
    private Object trueValue;
    private Object falseValue;

    public CBooleanCellComponent() {
        this(true, false);
    }

    public CBooleanCellComponent(Object trueValue, Object falseValue) {
        this.trueValue = trueValue;
        this.falseValue = falseValue;
        checkBox.setOpaque(false);
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public JComponent getComponent() {
        return checkBox;
    }

    @Override
    public void setValue(Object value) {
        checkBox.setSelected(ClassUtil.safeEquals(value, trueValue));
    }

    @Override
    public Object getValue() {
        return checkBox.isSelected() ? trueValue : falseValue;
    }

    @Override
    public String getTextByValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static CTableCellEditor createEditor(CBooleanCellComponent component) {
        return new CTableCellEditor(component);
    }

    public static CTableCellRenderer createRenderer(CBooleanCellComponent component) {
        return new CTableCellRenderer(component).setShowComponent(true);
    }

    public static CTableCellEditor createEditor() {
        return new CTableCellEditor(new CBooleanCellComponent());
    }

    public static CTableCellRenderer createRenderer() {
        return new CTableCellRenderer(new CBooleanCellComponent()).setShowComponent(true);
    }
}
