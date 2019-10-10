package org.pp.component.table;

import java.awt.*;

/**
 * 表格的行 列设置；背景 前景 字体等属性设置
 * @param <T>
 */
public abstract class PropSetting<T> {
	public Color getBackground(final T row) {
		return null;
	}
	public Color getForeground(final T row) {
		return null;
	}
	public Color getSelectedBackground(final T row) {
		return null;
	}
	public Color getSelectedForeground(final T row) {
		return getForeground(row);
	}
	public Color getBackground(final T row, boolean selected) {
		return selected ? getSelectedBackground(row) : getBackground(row);
	}
	public Color getForeground(final T row, boolean selected) {
		return selected ? getSelectedForeground(row) : getForeground(row);
	}
	
	public Font getFont(final T row, boolean selected) {
		return null;
	}
	
	public Font getFont(final T row, Font originFont, boolean selected) {
		return getFont(row, selected);
	}
}
