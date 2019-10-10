package org.pp.component.table.cell.cellComponent;

import org.pp.component.table.cell.cellRenderer.CTableCellRenderer;

import javax.swing.*;

public abstract class CAbstractTableCellComponent implements ITableCellComponent {

    public static <T> CTableCellRenderer createRenderer(final ITranslator<String, T> translator) {
        return new CTableCellRenderer(new CAbstractTableCellComponent() {

            @Override
            public String getTextByValue(Object value) {
                return translator.transfer((T) value);
            }

            @Override
            public JComponent getComponent() {
                return null;
            }

            @Override
            public void setValue(Object value) {

            }

            @Override
            public Object getValue() {
                return null;
            }
        });
    }
}
