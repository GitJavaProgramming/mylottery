package org.pp.component.table;


import org.pp.component.event.AbstractSelectChangeListener;
import org.pp.component.event.ISelectChangeListener;
import org.pp.component.event.SelectEvent;
import org.pp.component.table.cell.cellComponent.CBooleanCellComponent;
import org.pp.component.table.cell.cellComponent.ITableCellComponent;
import org.pp.component.table.cell.cellEditor.CTableCellEditor;
import org.pp.component.table.cell.cellRenderer.CTableCellRenderer;
import org.pp.util.ArrayUtil;
import org.pp.util.ClassUtil;
import org.pp.util.ExcelTxtUtil;
import practice.util.DataUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CGridTable<T> extends JTable {

    private ColumnInfo[] columns = new ColumnInfo[0];

    private static final int ROW_HEIGHT = 24;

    private List<T> data = new ArrayList<>();
    //    private List<T> data = new CopyOnWriteArrayList<>();
    private boolean indexStartWithZero = false;

    protected List<ISelectChangeListener<T>> checkListenerList;
    private boolean showHeaderCheck = true;
    private List<T> selectedRows = new CopyOnWriteArrayList<>();
    private boolean singleCheck = false;
    private T lastSelection;
    /* 整个表格是否可编辑 默认可以编辑 */
    private boolean editable = true;
    private PropSetting<T> rowPropSetting;
    private boolean columnSetPropFirst = true;

    public class CTableModel extends AbstractTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            // 表格可编辑，然后要看各列的可编辑状态设置
            column = column % columns.length;
            return columns[column].isEditable();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            columnIndex = columnIndex % columns.length;
            ColumnInfo col = columns[columnIndex];
            Object v = null;
            if (col.isUserColumn()) {// 自定义函数
                if (col.isSelectColumn()) {
                    v = isRowChecked(data.get(rowIndex))/*&& isRowSelected(rowIndex)*/;
                } else if (col.isIndexColumn()) {
                    v = rowIndex + (indexStartWithZero ? 0 : 1);
                } else if (col.isEmptyColumn()) {
                    v = "";
                }
            } else {
                v = getValue(rowIndex, columnIndex);
            }
            if (col.getFormat() != null) {
                v = col.getFormat().format(v);
            }
            return v;
        }

        private Object getValue(int rowIndex, int columnIndex) {
            T t = data.get(rowIndex);
            ColumnInfo col = columns[columnIndex];
            String name = col.getColName();
            Class<?> clazz = t.getClass();
            Object o = null;
            try {
                Field field = clazz.getDeclaredField(name);
                o = ClassUtil.doInvokeGetter(field, t);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return o;
        }

        @Override
        public void setValueAt(Object v, int rowIndex, int columnIndex) {
            columnIndex = columnIndex % columns.length;
            T t = data.get(rowIndex);
            ColumnInfo col = columns[columnIndex];
            if (col.isUserColumn()) {
                //TODO:
                //fireCheckChanged(rowIndex, datas.get(rowIndex));
                if (col.isSelectColumn()) {
                    checkRow((Boolean) v, data.get(rowIndex), rowIndex);
                }
                return;
            }
            String name = col.getColName();
            try {
                ClassUtil.doInvokeSetter(t.getClass().getDeclaredField(name), t, v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    /**
     * 构造函数
     *
     * @param columns
     */
    public CGridTable(ColumnInfo... columns) {
        this.columns = columns;
        this.setModel(new CTableModel());
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setTableHeader(new CTableHeader());
        this.setFillsViewportHeight(true);
        this.setRowHeight(ROW_HEIGHT);
        this.setRowMargin(1);
        this.setShowGrid(true); // 显示网格线
        putClientProperty("terminateEditOnFocusLost", true);
        setSurrendersFocusOnKeystroke(true);
        initColumnModels();
        resizeColumns();
        //设置列不可随容器组件大小变化自动调整宽度
//        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        this.getTableHeader().setSize(new Dimension(500, ROW_HEIGHT));
        addHeaderListener();
        addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if (e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED) {
                    resizeColumns();
                }
            }
        });
//        addCheckChangedEventListener(new AbstractSelectChangeListener<T>() {
//            @Override
//            public void selectedChanged(SelectEvent evt, int index, T what) {
//            }
//        });

//        //添加标格监听事件
//        addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                Point point = e.getPoint();
//                int columnIndex = columnAtPoint(point); //获取点击的列
//                if (columns[columnIndex].isSelectColumn()) {
//                    final int mousePressedRowIndex = rowAtPoint(point); //获取点击的行
//                    final JCheckBox jCheckBox = (JCheckBox) getComponentAt(point);
//                    if (jCheckBox.isSelected()) {
//                        removeRowSelectionInterval(mousePressedRowIndex, mousePressedRowIndex);
//                    } else {
//                        jCheckBox.setBackground(getSelectionBackground());
//                        addRowSelectionInterval(mousePressedRowIndex, mousePressedRowIndex);
//                    }
//                }
//            }
//        });
    }

    /* **************************************************table init************************************************** */

    public void initColumnModels() {
        if (columns == null || columns.length == 0) {
            return;
        }
        DefaultTableColumnModel ccm = new DefaultTableColumnModel();
        for (int i = 0; i < columns.length; i++) {
            final ColumnInfo c = columns[i];
            TableColumn tc = new TableColumn(i + columns.length);
            if (c.getEditor() != null) {
                tc.setCellEditor(c.getEditor());
            } else {
                if (!c.isSelectColumn()) {
                    tc.setCellEditor(new CTableCellEditor());
                }
            }
            if (c.getRenderer() != null) {
                CTableCellRenderer tableCellRenderer = (CTableCellRenderer) c.getRenderer();
                tc.setCellRenderer(tableCellRenderer);
            } else {
                if (!c.isSelectColumn()) {
                    CTableCellRenderer tableCellRenderer = new CTableCellRenderer();
                    tableCellRenderer.setHorizontalAlignment(c.getAlign().getAlign());
                    tc.setCellRenderer(tableCellRenderer);
                }
            }
            if (c.getHeaderRenderer() == null) { // 没有headerRenderer则使用默认
                if (!c.isSelectColumn()) {
                    // tableHeader 样式由tableHeader 设置ui制定
//                    tc.setHeaderRenderer(new CTableCellRenderer(new CTextCellComponent()));
                    // 设置非选择列表头对齐方式
//                    ((CTableCellRenderer) tc.getHeaderRenderer()).setHorizontalAlignment(c.getAlign().getAlign());
                }
            } else {
                tc.setHeaderRenderer(c.getHeaderRenderer());
            }
            if (c.isSelectColumn()) {
                tc.setHeaderRenderer(new CTableCellRenderer(new CBooleanCellComponent() {
                    private JCheckBox checkbox;

                    @Override
                    public void setValue(Object value) {
                        if (!(value instanceof Boolean) || value.equals(getCheckBox().isSelected())) {
                            return;
                        }
                        getCheckBox().setSelected(value.equals(true));
                        checkRows(getCheckBox().isSelected(), data);
                        //TODO:
//                        fireCheckChanged(-1, null);
                    }

                    @Override
                    public JComponent getComponent() {
                        return showHeaderCheck ? getCheckBox() : null;
                    }

                    protected JCheckBox getCheckBox() {
                        if (checkBox == null) {
                            checkBox = new JCheckBox();
                            checkBox.setOpaque(false);
                            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                        }

                        return checkBox;
                    }

                    private JCheckBox checkBox;
                }).setShowComponent(true));
                ((CTableCellRenderer) tc.getHeaderRenderer()).setHorizontalAlignment(c.getAlign().getAlign());
                int fixWidth = DataUtils.asInt(c.getWidth());
                tc.setPreferredWidth(fixWidth);
                tc.setMinWidth(fixWidth);
                tc.setMaxWidth(fixWidth);
            }
            ccm.addColumn(tc);
        }
        ccm.setColumnMargin(1);
        setColumnModel(ccm);
        changColumnHeaders();
    }

    protected void changColumnHeaders() {
        DefaultTableColumnModel tcm = (DefaultTableColumnModel) getColumnModel();
        for (int i = 0; i < columns.length; i++) {
            ColumnInfo gc = columns[i];
            TableColumn tc = tcm.getColumn(i);
            String extend = "";
            if (gc.getSort() == ColSortEnum.asc) {
                extend = " ▲";
            } else if (gc.getSort() == ColSortEnum.desc) {
                extend = " ▼";
            }
            tc.setHeaderValue(gc.getText() + extend);
            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(gc.getAlign().getAlign());
        }
        getTableHeader().repaint();
    }

    private void resizeColumns() {
//        返回单元格之间的水平间距和垂直间距。
        int space = getIntercellSpacing().width;
        int width = getSize().width;
        int cols = getColumnCount(); //columns.length
        width -= (space * (cols - 1));

        float total = 0F, totalWidth = 0F;
        for (int i = 0; i < cols; i++) {
            ColumnInfo c = columns[i];
            float cw = c.getWidth();
            int w = DataUtils.asInt(cw);
            if (c.isWidthFixed()) {
                width -= cw;
                getColumnModel().getColumn(i).setPreferredWidth(w);
                getColumnModel().getColumn(i).setMinWidth(w);
                getColumnModel().getColumn(i).setMaxWidth(w);
                getColumnModel().getColumn(i).setWidth(w);
            } else {
                total += columns[i].getWidth();
            }
        }
        for (int i = 0; i < cols; i++) {
            if (!columns[i].isWidthFixed() && columns[i].getWidth() != 0) {
                int w = DataUtils.asInt(width * columns[i].getWidth() / total);
                getColumnModel().getColumn(i).setPreferredWidth(w);
            }
        }
    }

    /*  添加表头事件监听 */
    private void addHeaderListener() {
        final JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);// 禁止列拖动
        header.addMouseListener(getHeaderListener());
        addSelectHeaderListener();
    }

    protected void addSelectHeaderListener() {
        getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!showHeaderCheck || singleCheck) {
                    return;
                }
                int index = getTableHeader().columnAtPoint(e.getPoint());
                if (index < 0) {
                    return;
                }
                TableColumn tc = getColumnModel().getColumn(index);
                if (columns[index].isSelectColumn()) {
//                    setRowSelectionInterval(0, getRowCount() - 1);
//                    clearSelection();
                    ITableCellComponent cellComp = ((CTableCellRenderer) tc.getHeaderRenderer()).getComponent();
                    JCheckBox checkBox = (JCheckBox) cellComp.getComponent();
                    cellComp.setValue(!checkBox.isSelected());
                    // 更新每一行checkbox editor值  注意render和editor值的不一致性
                    CTableCellEditor cellComp2 = ((CTableCellEditor) tc.getCellEditor());
                    cellComp2.setValue(checkBox.isSelected());
                    getTableHeader().repaint();
                } else if (tc.getHeaderRenderer() != null && tc.getHeaderRenderer() instanceof CTableCellRenderer) {
                    Component cellComp = ((CTableCellRenderer) tc.getHeaderRenderer()).getComponent().getComponent();
                    if (cellComp instanceof JButton)
                        ((JButton) cellComp).doClick();
                }
            }
        });
    }

    protected MouseListener getHeaderListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 双击排序
                if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() != 2)
                    return;

                headerClicked(getTableHeader().columnAtPoint(e.getPoint()));
            }
        };
    }

    protected void headerClicked(int index) {
        if (index < 0)
            return;
        index = index % columns.length;
        // 重新查询前检查有无待更新数据

        ColumnInfo gc = columns[index];

        // 切换顺序是 none->asc->desc-none;NO不参加轮换
        switch (gc.getSort()) {
            case no:
                return;
            case none:
                gc.setSort(ColSortEnum.asc);
                break;
            case asc:
                gc.setSort(ColSortEnum.desc);
                break;
            case desc:
                gc.setSort(ColSortEnum.none);
                break;
        }
        // 只允许单列排序
        for (int i = 0; i < columns.length; i++) {
            ColumnInfo c = columns[i];
            if (i != index && c.getSort() != ColSortEnum.no) {
                c.setSort(ColSortEnum.none);
            }

        }
        // 改变排列的头
        changColumnHeaders();
        // 执行抽取数据
//        query();
    }

    /* ***********************************************table operation*********************************************** */

    public void addColumn(ColumnInfo c, float width) {
        c.setWidth(width);
        addColumn(c);
    }

    public void addColumn(ColumnInfo col) {
        if (col == null) {
            return;
        }
        columns = ArrayUtil.arrayAppend(columns, col);
        initColumnModels();
    }

    public boolean isSingleCheck() {
        return singleCheck;
    }

    /**
     * 表头checkRows
     *
     * @param check
     * @param rows
     */
    public void checkRows(boolean check, List<T> rows) {
        for (T row : rows)
            checkRow(check, row, -1, false);
        fireCheckChanged(-1, null, rows);
    }

    private boolean checkRow(boolean check, T row, int index) {
        return checkRow(check, row, index, true);
    }

    protected boolean checkRow(boolean check, T row, int index, boolean fireAction) {
        if (isSingleCheck() && check)
            selectedRows.clear();
        int idx = getIndexInChecked(row);
        if (check && idx < 0) {
            selectedRows.add(row);
        } else if (!check && idx >= 0) {
            selectedRows.remove(idx);
        } else {
            return false;
        }
        if (fireAction) {
            fireCheckChanged(index, row, null);
        }
        revalidate();
        repaint();
        return true;
    }

    /* 反选 */
    public void inverseCheckedRows() {
        List<T> list = new ArrayList<>(data);
        list.removeAll(selectedRows);
        // 此处有并发访问控制 用copyOnWriteList 代替了selectedRows
        checkRows(false, selectedRows);
        checkRows(true, list);
        revalidate();
        repaint();
    }

    /* 取消选择项 */
    public void unChecked() {
        selectedRows.clear();
        revalidate();
        repaint();
    }

    /* 行是否选中 */
    public boolean isRowChecked(T row) {
        return getIndexInChecked(row) >= 0;
    }

    private int getIndexInChecked(T row) {
        int idx;
        if ((idx = selectedRows.indexOf(row)) >= 0) {
            return idx;
        }
        return -1;
    }

    public ColumnInfo getColumnAt(int index) {
        index = index % columns.length;
        if (index >= columns.length || index < 0) {
            return null;
        }
        return columns[index];
    }

    /**
     * 获取指定行的实例
     *
     * @param row 行索引
     * @return
     */
    public T getEntry(int row) {
        if (row < 0 || row >= data.size()) {
            return null;
        }
        return data.get(row);
    }

    /**
     * 获取当前选中的行数据，如果未选中，则返回null；
     *
     * @return
     */
    public T getSelectedEntry() {
        return this.getEntry(this.getSelectedRow());
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void deleteEntry() {
        if (!this.isEditable()) {
            throw new IllegalStateException("表格不可编辑。");
        }
        if (selectedRows == null || selectedRows.isEmpty()) {
            return;
        }
        int len = selectedRows.size();
        for (int i = 0; i < len; i++) {
            T entry = selectedRows.get(i);
            data.remove(entry);
        }
        selectedRows.clear();
        revalidate();
        repaint();
    }

    /* 获取表格是否可编辑 */
    public boolean isEditable() {
        return editable;
    }

    /**
     * 在表格最底部增加一个行
     */
    public void addEntry(T entry) {
        if (!this.isEditable()) {
            throw new IllegalStateException("表格不可编辑。");
        }
        data.add(entry);
        revalidate();
        repaint();
    }

    public void addEntryList(List<T> entryList) {
        if (!this.isEditable()) {
            throw new IllegalStateException("表格不可编辑。");
        }
        data.addAll(entryList);
        revalidate();
        repaint();
    }

    public void insertFrontOfEntry(T entry) {
        insertEntry(entry, true);
    }

    public void insertBackOfEntry(T entry) {
        insertEntry(entry, false);
    }

    public void insertEntry(T entry, boolean insertFront) {
        int latestSelectedRow = getSelectedRow();
        if (latestSelectedRow == -1) { // 如果没有选择行则插入到第一行
            if (insertFront) {
                latestSelectedRow = 0;
            } else {
                latestSelectedRow = data.size() - 1;
            }
        }
        insertEntryAt(latestSelectedRow, entry, insertFront);
    }

    private void insertEntryAt(int index, T entry, boolean insertFront) {
        if (!this.isEditable()) {
            throw new IllegalStateException("表格不可编辑。");
        }
        rangeCheck(index);
        moveOneStep(index, entry);
        if (insertFront) {
            data.set(index, entry);
            setRowSelectionInterval(index, index);
        } else {
            data.set(index + 1, entry);
            setRowSelectionInterval(index + 1, index + 1);
        }
        revalidate();
        repaint();
    }

    private void moveOneStep(final int insertIndex, T entry) {
        rangeCheck(insertIndex);
        for (int i = data.size(); i > insertIndex; i--) {
            if (i == data.size()) { // 最后一个元素超出list范围  添加
                data.add(i, data.get(i - 1));
            } else {
//                set -> rangeCheck
                data.set(i, data.get(i - 1));
            }
        }
    }

    private void rangeCheckForAdd(int index) {
        if (index > data.size() || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + data.size();
    }

    private void rangeCheck(int index) {
        if (index >= data.size())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /* 清空表格 */
    public void clear() {
        clearSelection();
        data.clear();
        selectedRows.clear();
        revalidate();
        repaint();
    }

    public void setIndexStartWithZero(boolean indexStartWithZero) {
        this.indexStartWithZero = indexStartWithZero;
    }

    public void scrollToSelectedVisible() {
        scrollRectToVisible(getCellRect(getSelectedRow(), getSelectedColumn(), true));
    }

    /* ************************************************export excel************************************************ */
    public boolean exportExcel() {
        return exportExcel(this);
    }

    public boolean exportExcel(Component dlgParent) {
        return exportExcel(dlgParent, true);
    }

    public boolean exportExcel(Component dlgParent, boolean requeryAllData) {
        return exportExcel(dlgParent, requeryAllData, true);
    }

    public boolean exportExcel(Component dlgParent, boolean requeryAllData, boolean showMsgDialog) {
        return exportExcel(dlgParent, requeryAllData, showMsgDialog, null);
    }

    public boolean exportExcel(Component dlgParent, boolean requeryAllData, boolean showMsgDialog, IQualifier<T> qualifier) {
        return ExcelTxtUtil.exportTable(this, dlgParent, requeryAllData, showMsgDialog, qualifier);
    }

    /* ************************************************event listener************************************************ */
    protected void fireCheckChanged(int index, T what, List<T> changes) {
        for (ISelectChangeListener<T> listener : getCheckChangedListeners()) {
            if (listener instanceof AbstractSelectChangeListener)
                ((AbstractSelectChangeListener<T>) listener).setRowsChanged(changes);
            listener.selectedChanged(new SelectEvent(this, 0), index, what);
        }
    }

    public List<ISelectChangeListener<T>> getCheckChangedListeners() {
        if (checkListenerList == null) {
            checkListenerList = new ArrayList<ISelectChangeListener<T>>();
        }
        return checkListenerList;
    }

    public void addCheckChangedEventListener(ISelectChangeListener<T> listener) {
        getCheckChangedListeners().add(listener);
    }

    protected void fireSelectChange(EventObject e, int row) {
        T t = getSelectedEntry();
        for (ISelectChangeListener<T> scl : checkListenerList)
            scl.selectedChanged(new SelectEvent(e.getSource(), lastSelection, e instanceof AWTEvent ? ((AWTEvent) e).getID() : -1, 1), getSelectedRow(), t);
        lastSelection = t;
    }

    /* 添加一个行选择变更监听 */
    public void addSelectChangeListener(ISelectChangeListener<T> listener) {
        if (this.checkListenerList == null) {
            this.checkListenerList = new ArrayList<ISelectChangeListener<T>>();
            getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        fireSelectChange(e, e.getFirstIndex());
                    }
                }
            });
        }
        this.checkListenerList.add(listener);
    }

    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        if (getColumnAt(columnIndex).isSelectColumn()) {
            return;
        }
        if (getSelectedRowCount() > 1 || rowIndex < 0 || rowIndex > getRowCount() || columnIndex < 0 || columnIndex > getColumnCount()) {
            return;
        }
        if (getColumnAt(columnIndex).isIndexColumn()) {
//            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            columnIndex = columnIndex % columns.length;
            T row = getEntry(rowIndex);
            boolean isRowChecked = isRowChecked(row);// 选择当前行时，行的checked选中状态
            if (isRowChecked) { // 当前是选中，点击后checkbox取消选中
                checkRow(false, row, columnIndex);
            } else {// 当前没选中，点击后checkbox选中
                checkRow(true, row, columnIndex);
            }
        }
//        int[] ints = getSelectedRows();
//        for(int i : ints) {
//            removeRowSelectionInterval(i, i);
//        }
//        checkRows(true, selectedRows);
        // 表格行选择时，点击前面checkbox -> 点击表头checkbox勾选表头 -> 取消勾选 当前选中行的checkbox无法取消
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component r = super.prepareRenderer(renderer, row, column);
        JComponent c = (JComponent) r;
        if (r instanceof CTableCellRenderer && ((CTableCellRenderer) r).isShowComponent()) {
            c = ((CTableCellRenderer) r).getComponent().getComponent();
        }
        ColumnInfo columnInfo = columns[column % columns.length];
        if (columnInfo.isSelectColumn()) { // 去除选中行的选择列的背景
            c.setBackground(null);
            return c;
        }
        PropSetting<T> columnPropSetting = (PropSetting<T>) columns[column].getPropSetting();
        if (rowPropSetting != null || columnPropSetting != null) {
            boolean selected = isCellSelected(row, column);
            Color fg = null;
            Color bg = null;
            Font f = null;
            T entry = getEntry(row);
            PropSetting<T> first, second;
            if (columnSetPropFirst) { // 列优先
                first = columnPropSetting;
                second = rowPropSetting;
            } else { // 行优先设置
                first = rowPropSetting;
                second = columnPropSetting;
            }
            // 默认行优先
            if (second != null) {
                bg = second.getBackground(entry, selected);
                fg = second.getForeground(entry, selected);
                f = second.getFont(entry, r.getFont(), selected);
            }
            if (first != null) {
                if (bg == null) {
                    bg = first.getBackground(entry, selected);
                }
                if (fg == null) {
                    fg = first.getForeground(entry, selected);
                }
                if (f == null) {
                    f = first.getFont(entry, r.getFont(), selected);
                }
            }

            if (c != null) {
                c.setBackground(bg != null ? bg : (selected ? getSelectionBackground() : getBackground()));
                c.setForeground(fg != null ? fg : (selected ? getSelectionForeground() : getForeground()));
            }

            if (c != null && f != null) {
                c.setFont(f);
            }
        }

        return r;
    }

    /* getter setter */
    public PropSetting<T> getRowPropSetting() {
        return rowPropSetting;
    }

    public void setRowPropSetting(PropSetting<T> rowPropSetting) {
        this.rowPropSetting = rowPropSetting;
    }

    public ColumnInfo[] getVisibleColumns() {
        return columns;
    }

    public ColumnInfo getVisibleColumnAt(int index) {
        index = index % columns.length;
        if (index >= columns.length || index < 0) {
            return null;
        }
        return columns[index];
    }

    public boolean isColumnSetPropFirst() {
        return columnSetPropFirst;
    }

    public void setColumnSetPropFirst(boolean columnSetPropFirst) {
        this.columnSetPropFirst = columnSetPropFirst;
    }

//    @Override
//    public void setSelectionBackground(Color selectionBackground) {
////        super.setSelectionBackground(getBackground());
//    }
//
//    @Override
//    public void setSelectionForeground(Color selectionForeground) {
////        super.setSelectionForeground(getForeground());
//    }

    /* 返回表格中所有的数据，数据不可更改 */
    public List<T> getData() {
        return Collections.unmodifiableList(data);
    }

    public List<T> getCheckedRows() {
        return Collections.unmodifiableList(selectedRows);
    }
}
