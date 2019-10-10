package org.pp.component.list.listCellRenderer;

import javax.swing.*;
import java.awt.*;

public class LabelRenderer<E> implements ListCellRenderer<E> {
    JLabel label = new JLabel() {
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            super.paintComponent(g);
        }

    };

    public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
        label.setPreferredSize(new Dimension(100, 24));
        label.setText("  " + value.toString());
        label.setFont(list.getFont());
        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
        } else {
            label.setBackground(list.getBackground());
        }
        label.repaint(100);
        return label;
    }
}
