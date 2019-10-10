package org.pp.component.list.listCellRenderer;

import javax.swing.*;
import java.awt.*;


public class RadioRenderer<E> implements ListCellRenderer<E> {
    JRadioButton radio = new JRadioButton();

    public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
        radio.setSelected(isSelected);
        radio.setText(value.toString());
        radio.setFont(list.getFont());
        if (isSelected) {
            radio.setBackground(list.getSelectionBackground());
        } else {
            radio.setBackground(list.getBackground());
        }
        return radio;
    }
}
