package org.pp.component.table.cell.cellComponent;

import javax.swing.*;

public interface ITableCellComponent {
    /**
     * 如果显示字符串，renderer需实现这个函数
     * 要导出excel的表格，所有renderer需实现此函数
     *
     * @param value
     */
    String getTextByValue(Object value);

    /**
     * 如果显示控件，renderer需实现这个函数
     * editor需实现这个函数
     *
     * @return renderer或editor显示的界面
     */
    JComponent getComponent();

    /**
     * 如果显示控件，renderer需实现这个函数
     * editor需实现这个函数
     *
     * @param value
     */
    void setValue(Object value);

    /**
     * renderer无需实现此函数
     * editor需实现这个函数
     *
     * @return 当前选中要传给table的value
     */
    Object getValue();
}
