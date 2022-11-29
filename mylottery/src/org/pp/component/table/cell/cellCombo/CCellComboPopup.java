package org.pp.component.table.cell.cellCombo;

import org.pp.component.list.CList;
import org.pp.component.table.IFilterComponent;
import org.pp.component.table.cell.cellComponent.ITableCellComponent;
import org.pp.component.table.cell.cellEditor.CTableCellEditor;
//import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;

public class CCellComboPopup extends JPopupMenu implements ComboPopup, PopupMenuListener {

    /* popupMenu 鼠标事件处理器 */
    protected class InvocationMouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e) || !comboBox.isEnabled()) {
                return;
            }
//            if(comboBox.getDropdownComponent() instanceof IFilterComponent) {
//                return;
//            }
            togglePopup();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Point point = e.getPoint();
            if (comboBox.isEditable()) {
                if (point.getY() < 0) {
                    hide();
                } else if (point.getX() >= 0) {
                    int targetWidth = comboBox.getWidth() - comboBox.getEditor().getEditorComponent().getWidth();
                    int targetHeight = comboBox.getHeight();
                    if (point.getX() <= targetWidth && point.getY() < targetHeight) {
                        hide();
                    }
                }
            } else {
                if (point.getX() >= 0) {
                    if (point.getY() <= 0) {
                        hide();
                    } else {
                        int targetWidth = comboBox.getWidth() - comboBox.getEditor().getEditorComponent().getWidth();
                        int targetHeight = comboBox.getHeight();
                        if (point.getX() <= targetWidth && point.getY() < targetHeight) {
                            hide();
                        }
                    }
                }
            }
        }
    }

    /* 下拉框组件 鼠标事件处理器 当下拉框组件为JPanel时注意鼠标事件源是否是当前JPanel */
    public class SelectListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            setCellComboBoxValue();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Point point = e.getPoint();
            if (comboBox.isEditable()) {
                if (point.getX() <= 0) {
                    hide();
                } else {
                    if (point.getY() <= 0) {
                        boolean flag = Math.abs(point.getY()) <= comboBox.getHeight() && point.getX() >= 0 && point.getX() <= comboBox.getWidth();
                        if (!flag) {
                            hide();
                        }
                    } else {
                        if (point.getY() >= comboBox.getDropdownComponent().getComponent().getHeight() || point.getX() > comboBox.getWidth()) {
                            hide();
                        }
                    }
                }
            } else {
                // 判断退出popupMenu的鼠标是否在下拉框中
                boolean inDropDown = comboBox.getDropdownComponent().getComponent().contains(point);
                if (inDropDown) {
                    return;
                }
                hide();
            }
        }
    }

    public CCellComboPopup(JComboBox comboBox) {
        this.comboBox = (CCellComboBox) comboBox;
        final ITableCellComponent component = this.comboBox.getDropdownComponent();
        if (component != null && component.getComponent() != null) {
            setBorder(BorderFactory.createLineBorder(Color.BLUE));
            setLayout(new BorderLayout());
            setLightWeightPopupEnabled(this.comboBox.isLightWeightPopupEnabled());
            scrollPane = new JScrollPane(component.getComponent());
            scrollPane.setBorder(null);
            add(scrollPane, BorderLayout.CENTER);
            component.getComponent().addMouseListener(new SelectListener());
            addPopupMenuListener(this);
        }
    }

    private void setCellComboBoxValue() {
        comboBox.setSelectedItem(comboBox.getDropdownComponent().getValue());
    }

    protected void togglePopup() {
        if (isVisible()) {
            hide();
        } else {
            show(); //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public void hide() {
        if (!isVisible()) {
            return;
        }
        stopTableCellEditing();
        setVisible(false);
    }

    public void show() {
        if (isVisible()) {
            return;
        }
        updatePopup();
        show(comboBox, 0, comboBox.getHeight());
    }

    protected void updatePopup() {
        if (comboBox.getDropdownComponentWidth() > comboBox.getWidth()) {
            comboBox.setSize(comboBox.getDropdownComponentWidth(), comboBox.getHeight());
        }
        if (comboBox.getDropdownComponent() instanceof CList) {
            CList list = (CList) comboBox.getDropdownComponent();
//                list.getFixedCellHeight()
            int rowHeight = comboBox.getTable().getRowHeight(rowEditing);
            setPreferredSize(new Dimension(comboBox.getSize().width, (list.getData().size() + 1) * rowHeight));
        } else {
//            setPreferredSize(comboBox.getSize());
//            setPreferredSize(new Dimension(comboBox.getSize().width, 200));
        }
    }

    private void stopTableCellEditing() {
        if (comboBox != null && comboBox.getTable() != null) {
            comboBox.getTable().repaint();
            CTableCellEditor tableCellEditor = (CTableCellEditor) comboBox.getTable().getCellEditor();
            tableCellEditor.stopCellEditing();
        }
    }

    @Override
    public JList getList() {
        return list;
    }

    @Override
    public MouseMotionListener getMouseMotionListener() {
        if (mouseMotionListener == null) {
            mouseMotionListener = new MouseMotionAdapter() {
            };
        }
        return mouseMotionListener;
    }

    @Override
    public KeyListener getKeyListener() {
        return null;
    }

    @Override
    public void uninstallingUI() {
    }

    @Override
    public MouseListener getMouseListener() {
        if (mouseListener == null) {
            mouseListener = new InvocationMouseHandler();
        }
        return mouseListener;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        // popupMenu 切换时触发？
        hide();
//        Component component = SwingUtilities2.compositeRequestFocus(comboBox);
//        System.out.println(component.getName());
    }

    protected JList list = new JList();
    protected CCellComboBox comboBox;
    protected JScrollPane scrollPane;

    protected MouseMotionListener mouseMotionListener;
    protected MouseListener mouseListener;
    protected int rowEditing;
    protected int columnEditing;
}  