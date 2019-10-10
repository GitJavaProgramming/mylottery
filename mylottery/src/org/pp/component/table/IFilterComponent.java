package org.pp.component.table;

import javax.swing.event.DocumentEvent;

public interface IFilterComponent {
    void filter(DocumentEvent e);

    boolean toFilter();
}
