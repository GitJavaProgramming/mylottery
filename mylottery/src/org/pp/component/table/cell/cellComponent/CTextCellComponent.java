package org.pp.component.table.cell.cellComponent;

import javax.swing.*;

public class CTextCellComponent extends JTextField implements ITableCellComponent {

    public CTextCellComponent() {
        this.setHorizontalAlignment(SwingConstants.CENTER);
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
        setText(value == null ? "" : value.toString());
    }

    @Override
    public Object getValue() {
        return getText();
    }
}
