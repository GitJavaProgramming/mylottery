package org.pp.component.table;

import javax.swing.*;

/**
 * renderer中内容的水平对齐方式枚举
 */
public enum CellAlignEnum {
    /**
     * 居左
     */
    left(SwingConstants.LEFT),
    /**
     * 居中,默认值
     */
    center(SwingConstants.CENTER),
    /**
     * 居右
     */
    right(SwingConstants.RIGHT);

    /**
     * 对应swing中的常量值
     */
    private int align;

    private CellAlignEnum(int i) {
        this.align = i;
    }

    public int getAlign() {
        return align;
    }
}
