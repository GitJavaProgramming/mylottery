package org.pp.test;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-29
 * Time: 下午7:55
 * To change this template use File | Settings | File Templates.
 */
public class LabelColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    private Font font;
    private Color fontColor;
    private JLabel lblRender;

    public LabelColumn(TableColumn tc, Font font, Color fontColor, int alignment) {
        lblRender = new JLabel();
        lblRender.setOpaque(true);
        tc.setCellEditor(this);
        tc.setCellRenderer(this);
        if (font != null)
            this.font = font;
        if (fontColor != null)
            this.fontColor = fontColor;
        if (alignment != -1)
            lblRender.setHorizontalAlignment(alignment);
    }

    public LabelColumn(TableColumn tc, Font font) {
        this(tc, font, null, SwingConstants.LEFT);
    }

    public LabelColumn(TableColumn tc, Color fontColor) {
        this(tc, null, fontColor, SwingConstants.LEFT);
    }

    public LabelColumn(TableColumn tc, int alignment) {
        this(tc, null, null, alignment);
    }

    public LabelColumn(TableColumn tc) {
        this(tc, null, null, SwingConstants.LEFT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        lblRender.setText(value != null ? value.toString() : "");
        lblRender.setFont(this.font == null ? table.getFont() : this.font);
        lblRender.setForeground(this.fontColor == null ? table.getForeground() : this.fontColor);
        lblRender.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return lblRender;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        // TODO Auto-generated method stub
        return null;
    }

}