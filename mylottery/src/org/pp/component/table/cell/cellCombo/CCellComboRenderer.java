package org.pp.component.table.cell.cellCombo;

import org.pp.component.table.cell.cellComponent.ITableCellComponent;

import javax.swing.*;
import java.awt.*;

public class CCellComboRenderer extends DefaultListCellRenderer {

    CCellComboRenderer(ITableCellComponent component) {
        this.component = component;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            return super.getListCellRendererComponent(list, component.getTextByValue(value), index, isSelected, cellHasFocus);
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

    protected ITableCellComponent component;
}  
