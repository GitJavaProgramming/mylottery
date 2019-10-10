package org.pp.component;

import javax.swing.*;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-4-2
 * Time: 下午7:00
 * To change this template use File | Settings | File Templates.
 */
public class CCombo<E> extends JComboBox<E> {

    private boolean nullAble = false;

    public CCombo() {
        this(false);
    }

    public CCombo(Vector<E> items) {
        super(items);
    }

    public CCombo(boolean nullAble) {
        super();
        this.setNullAble(nullAble);
    }

    public CCombo(E... items) {
        this(false, items);
    }

    public CCombo(boolean nullAble, E... items) {
        this.setNullAble(nullAble);
        for (E nv : items) {
            this.addItem(nv);
        }
    }

    public CCombo(List<E> items) {
        this(false, items);
    }

    /**
     * 构造函数
     *
     * @param items
     */
    public CCombo(boolean nullAble, List<E> items) {
        if (nullAble) {
            addNullItem();
        }
        addItems(items);
    }

    public void setData(List<E> data) {
        removeAllItems();
        if (nullAble) {
            addNullItem();
        }
        addItems(data);
    }

    public void addItems(List<E> data) {
        if (data != null) {
            for (E i : data) {
                addItem(i);
            }
        }
    }

    public void addNullItem() {
        this.addItem(null);
    }

    public boolean isSelectedNull() {
        return getSelectedItem() == null;
    }

    public boolean isnullAble() {
        return nullAble;
    }

    public void setNullAble(boolean nullAble) {
        this.nullAble = nullAble;
    }

    @Override
    public void setPopupVisible(boolean v) {
        if (isEnabled() || !v)
            getUI().setPopupVisible(this, v);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled)
            setPopupVisible(false);
    }

}
