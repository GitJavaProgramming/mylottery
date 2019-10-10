package org.pp.component.event;

import java.util.EventListener;

public interface ISelectChangeListener<T> extends EventListener {
	public void selectedChanged(SelectEvent evt, int index, T what);
}
