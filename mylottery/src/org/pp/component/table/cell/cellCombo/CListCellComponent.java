package org.pp.component.table.cell.cellCombo;

import org.pp.component.list.CList;
import org.pp.component.table.IFilterComponent;
import org.pp.component.table.cell.cellComponent.ITableCellComponent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-4-5
 * Time: 下午4:13
 * To change this template use File | Settings | File Templates.
 */
public class CListCellComponent<E> extends CList<E> implements ITableCellComponent, IFilterComponent {

    public CListCellComponent() {
        super();
    }

    public CListCellComponent(List<E> data) {
        super(data);
    }

    @Override
    public String getTextByValue(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void setValue(Object value) {
        this.clearSelection();
        String values = value.toString();
        String[] array = values.split(",");
        int len = array.length;
        for (int i = 0; i < len; i++) {
            setSelectedValue(array[i], true);
        }
    }

    @Override
    public Object getValue() {
        List<E> list = getSelectedValuesList();
        if (list.isEmpty()) {
            return defaultValue;
        }
        return list.toString().replaceAll("\\[| |]", "");
    }

    private Object defaultValue = "0";

    public Object getDefaultValue() {
        return defaultValue;
    }

    public CListCellComponent setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public void filter(DocumentEvent e) {

    }

    @Override
    public boolean toFilter() {
        return false;
    }
}
