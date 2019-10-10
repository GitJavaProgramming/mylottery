package org.pp.component.list.listCellRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-29
 * Time: 下午9:46
 * To change this template use File | Settings | File Templates.
 */
public class CheckRenderer<E> implements ListCellRenderer<E> {
    JCheckBox check = new JCheckBox();

    public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {

        check.setSelected(isSelected);
        check.setText(value.toString());
        check.setFont(list.getFont());
        if (isSelected) {
            check.setBackground(list.getSelectionBackground());
        } else {
            check.setBackground(list.getBackground());
        }
        return check;
    }
}