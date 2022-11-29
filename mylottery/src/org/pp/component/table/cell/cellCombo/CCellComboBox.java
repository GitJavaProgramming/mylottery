package org.pp.component.table.cell.cellCombo;

//import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
//import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;
import org.pp.component.CCombo;
import org.pp.component.table.IComboListComponent;
import org.pp.component.table.IFilterComponent;
import org.pp.component.table.cell.cellComponent.ITableCellComponent;
import org.pp.component.table.cell.cellEditor.CTableCellEditor;
import org.pp.util.ClassUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ��tree��checktree��combobox�ؼ�
 * settree��tree��ѡ��checktree��ѡ
 * canSelectNoneLeafֻ��tree��Ч
 * setSelectedItem����treepath
 * �������û�ȡֻ��checktree��Ч
 *
 * @author chenhb
 * @version 1.0.0
 * @since 1.0.0
 */
public class CCellComboBox extends CCombo implements ITableCellComponent {


    class MetalJTreeComboBoxUI extends MetalComboBoxUI {
        protected ComboPopup createPopup() {
            return new CCellComboPopup(comboBox);
        }
    }

//    class WindowsJTreeComboBoxUI extends WindowsComboBoxUI {
//        protected ComboPopup createPopup() {
//            return new CCellComboPopup(comboBox);
//        }
//    }
//
//    class MotifJTreeComboBoxUI extends MotifComboBoxUI {
//        private static final long serialVersionUID = 6284622455212671632L;
//
//        protected ComboPopup createPopup() {
//            return new CCellComboPopup(comboBox);
//        }
//    }

    public CCellComboBox(final ITableCellComponent cellComponent, JTable table) {
        setTable(table);
        setDropdownComponent(cellComponent);
        // 如果要让comboBox可编辑 组件必须实现 IFilterComponent  接口
        if (cellComponent instanceof IFilterComponent /*&& ((IFilterComponent) cellComponent).toFilter()*/) {
            setEditable(true);
            // todo: CCellComboPopup textField.addMouseListener
            setEditor(new BasicComboBoxEditor()); // 主动设置editor 否则CCellComboPopup找不到editor无法为其注册mouse事件，控制hide
            Component component1 = getEditor().getEditorComponent();
            final JTextField textField = (JTextField) component1;
            if (!((IFilterComponent) cellComponent).toFilter()) {
                textField.setEditable(false);
            }
            textField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    Point point = e.getPoint();
                    if (point.getX() < 0 || point.getY() < 0) {
                        hidePopupMenu();
                    }
                }
            });
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void removeUpdate(DocumentEvent e) {
                    filter(e);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    filter(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filter(e);
                }

                private void filter(DocumentEvent e) {
                    ((IFilterComponent) cellComponent).filter(e);
                    if (isShowing()) {
                        setPopupVisible(true);
                        getEditor().getEditorComponent().requestFocusInWindow();
                    }
                }
            });
        } else {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    Point point = e.getPoint();
                    if (point.getX() < 0 || point.getY() < 0) {
                        hidePopupMenu();
                    }
                }
            });
        }
        if (cellComponent instanceof IComboListComponent) {
            getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP)
                        ((IComboListComponent) cellComponent).selectPreviousItem();
                    if (e.getKeyCode() == KeyEvent.VK_DOWN)
                        ((IComboListComponent) cellComponent).selectNextItem();
                }
            });
        }
        updateUI();
    }

    private void hidePopupMenu() {
        if (!isPopupVisible()) {
            return;
        }
        CTableCellEditor tableCellEditor = (CTableCellEditor) getTable().getCellEditor();
        tableCellEditor.stopCellEditing();
        setPopupVisible(false);
    }

    public ITableCellComponent getDropdownComponent() {
        return dropdownComponent;
    }

    public int getDropdownComponentWidth() {
        return dropdownComponentWidth;
    }

    public void setDropdownComponent(final ITableCellComponent dropdownComponent) {
        this.dropdownComponent = dropdownComponent;
        if (dropdownComponent != null) {
            dropdownComponentWidth = dropdownComponent.getComponent().getWidth();
        }
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
        Font tableFont = table.getFont();
//        setFont(new Font(tableFont.getName(), tableFont.getStyle(), tableFont.getSize() - 3));
    }

    @Override
    public void updateUI() {
        ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
        if (cui instanceof MetalComboBoxUI) {
            cui = new MetalJTreeComboBoxUI();
        } /*else if (cui instanceof MotifComboBoxUI) {
            cui = new MotifJTreeComboBoxUI();
        } else {
            cui = new WindowsJTreeComboBoxUI();
        }*/
        setUI(cui);
    }

    @Override
    public void setSelectedItem(Object item) {
        if (!ClassUtil.safeEquals(item, selectedItemReminder)) {
            String text = dropdownComponent.getTextByValue(item);
            dropdownComponent.setValue(item);
            getEditor().setItem(text);
            if (selectedItemReminder != dataModel.getSelectedItem()) {
                selectedItemChanged();
            }
        }
//        fireActionEvent();
    }

    @Override
    public String getTextByValue(Object value) {
        return dropdownComponent.getTextByValue(value);
    }

    @Override
    public Object getValue() {
        return dropdownComponent.getValue();
    }

    @Override
    public void setValue(Object value) {
        setSelectedItem(value);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    private ITableCellComponent dropdownComponent;
    private int dropdownComponentWidth;
    private JTable table;
}
