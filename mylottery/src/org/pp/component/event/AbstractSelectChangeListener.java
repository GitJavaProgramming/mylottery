package org.pp.component.event;

import java.util.List;

public abstract class AbstractSelectChangeListener<T> implements ISelectChangeListener<T> {
	public List<T> getRowsChanged() {
		return rowsChanged;
	}

	public void setRowsChanged(List<T> rowsChanged) {
		this.rowsChanged = rowsChanged;
	}
	
	private List<T> rowsChanged;
}
