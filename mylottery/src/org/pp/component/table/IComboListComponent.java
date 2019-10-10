package org.pp.component.table;

/**
 * 作为一个下拉列表  键盘方向键监听
 */
public interface IComboListComponent {
    void selectPreviousItem();

    void selectNextItem();
}


// GridTable实现
//    @Override
//    public void selectPreviousItem() {
//        if (getRowCount() == 0)
//            return;
//        int row = getSelectedRow() - 1;
//        if (row >= getRowCount() || row < 0)
//            row = getRowCount() - 1;
//        setRowSelectionInterval(row, row);
//        scrollToSelectedVisible();
//    }
//
//    @Override
//    public void selectNextItem() {
//        if (getRowCount() == 0)
//            return;
//        int row = getSelectedRow() + 1;
//        if (row >= getRowCount() || row < 0)
//            row = 0;
//        setRowSelectionInterval(row, row);
//        scrollToSelectedVisible();
//    }