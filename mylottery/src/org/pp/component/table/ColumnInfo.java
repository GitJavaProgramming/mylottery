package org.pp.component.table;

import org.pp.component.table.cell.cellComponent.CBooleanCellComponent;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.text.DecimalFormat;
import java.text.Format;

public class ColumnInfo {

    private static final String USER_COLUMN_PREFIX = "#";

    private static final String INDEX_COLUMN_NAME = USER_COLUMN_PREFIX + "index";
    private static final String SELECT_COLUMN_NAME = USER_COLUMN_PREFIX + "select";
    private static final String EMPTY_COLUMN_NAME = USER_COLUMN_PREFIX + "empty";

    /* 默认列宽 */
    private static final float DEFAULT_WIDTH = 25;

    /* 实体字段名称 */
    private String colName;
    /* 列标题 */
    private String text;
    /* 列宽 */
    private float width = DEFAULT_WIDTH;
    /* 默认排序设置 */
    private ColSortEnum sort = ColSortEnum.none;
    /* 默认对齐方式 */
    private CellAlignEnum align = CellAlignEnum.center;
    /* 本列是否支持编辑 */
    private boolean editable = false;
    /* 列中单元格的编辑器,没有则此列不能编辑 */
    private TableCellEditor editor = null;
    /* 列中数值单元格的渲染器 */
    private TableCellRenderer renderer;
    private TableCellRenderer headerRenderer;
    private PropSetting<?> propSetting;
    /* 格式化 */
    private Format format;
    /* 列宽长度固定，一经指定将无法变动 */
    private boolean isWidthFixed = false;

    private boolean export = true;
    private boolean exportReadonly = false;
    private ExportType exportType = ExportType.Text;

    public static enum ExportType {
        Number,
        Text
    }

    public ColumnInfo() {
//        if (headerRenderer == null) {
//            headerRenderer = new CTableCellRenderer(new CTextCellComponent());
////            ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
//        }
    }

    /**
     * 构造函数
     *
     * @param colName 列名(colName)
     * @param text    显示文字
     */
    public ColumnInfo(String colName, String text) {
        this.colName = colName;
        this.text = text;
    }

    /**
     * 构造函数
     *
     * @param colName 列名(colName)
     * @param text    显示文字
     * @param sort    该列的排序设置
     * @param align   该列的呈现对齐设置
     */
    public ColumnInfo(String colName, String text, ColSortEnum sort, CellAlignEnum align) {
        this.colName = colName;
        this.text = text;
        this.sort = sort;
        this.align = align;
    }

    public static ColumnInfo createSelectColumn() {
        return new ColumnInfo(SELECT_COLUMN_NAME, "", ColSortEnum.no, CellAlignEnum.center).setWidthFixed(true).
                setWidth(20).setSort(ColSortEnum.no).setEditable(true).setExport(false).
                setEditor(CBooleanCellComponent.createEditor(/*cBooleanCellComponent*/)). // 不能使用共享的cellComponent 否则会出现样式紊乱
                setRenderer(CBooleanCellComponent.createRenderer(/*cBooleanCellComponent*/));
    }

    public static ColumnInfo createIndexColumn() {
        return new ColumnInfo(INDEX_COLUMN_NAME, "序号", ColSortEnum.no, CellAlignEnum.center).
                setFormat(new DecimalFormat("0 ")).setWidth(40).setSort(ColSortEnum.no).setWidthFixed(true);
    }

    public static ColumnInfo createEmptyColumn(String text) {
        return new ColumnInfo(EMPTY_COLUMN_NAME, text).setSort(ColSortEnum.no).setWidthFixed(true);
    }

    public boolean isSelectColumn() {
        return getColName().equals(SELECT_COLUMN_NAME);
    }

    public boolean isIndexColumn() {
        return getColName().equals(INDEX_COLUMN_NAME);
    }

    public boolean isEmptyColumn() {
        return getColName().equals(EMPTY_COLUMN_NAME);
    }

    public boolean isUserColumn() {
        return getColName().startsWith(USER_COLUMN_PREFIX);
    }

    /**
     * 该列是否可编辑，当editor!=null时，该列是否可编辑值，由editable决定
     *
     * @param editable
     */
    public ColumnInfo setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    /**
     * 设置列的宽度权重
     *
     * @param width
     * @return
     */
    public ColumnInfo setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * 设置该列的排序设置，一般列初始不使用排序，如要禁止排序，则需显式的使用ColSortEnum.no
     *
     * @param sort
     */
    public ColumnInfo setSort(ColSortEnum sort) {
        this.sort = sort;
        return this;
    }

    /**
     * 该列对应的单元格数据编辑器
     *
     * @param editor
     */
    public ColumnInfo setEditor(TableCellEditor editor) {
        this.editor = editor;
        this.setEditable(this.editor != null);
        return this;
    }

    /**
     * 设置单元格呈现解释器，当取出数据是“键”，用“值”呈现时，需要使用自定义的呈现解释器,比如在应用中使用  参数表、代码表等的情况
     *
     * @param renderer
     */
    public ColumnInfo setRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
        return this;
    }

    /**
     * 当使用内置呈现（通常是Label）时,对数值转化为字符串时所使用的格式化信息
     *
     * @param format
     */
    public ColumnInfo setFormat(Format format) {
        this.format = format;
        return this;
    }

    public ColumnInfo setWidthFixed(boolean fixed) {
        this.isWidthFixed = fixed;
        return this;
    }

    public CellAlignEnum getAlign() {
        return align;
    }

    public String getColName() {
        return colName;
    }

    public TableCellEditor getEditor() {
        return editor;
    }

    public boolean isEditable() {
        return editable;
    }

    public Format getFormat() {
        return format;
    }

    public TableCellRenderer getRenderer() {
        return renderer;
    }

    public ColSortEnum getSort() {
        return sort;
    }

    public String getText() {
        return text;
    }

    public float getWidth() {
        return width;
    }

    public boolean isWidthFixed() {
        return isWidthFixed;
    }

    public ColumnInfo setAlign(CellAlignEnum align) {
        this.align = align;
        return this;
    }

    public ColumnInfo setText(String text) {
        this.text = text;
        return this;
    }

    public ColumnInfo setColName(String colName) {
        this.colName = colName;
        return this;
    }

    public TableCellRenderer getHeaderRenderer() {
        return headerRenderer;
    }

    public ColumnInfo setHeaderRenderer(TableCellRenderer headerRenderer) {
        this.headerRenderer = headerRenderer;
        return this;
    }

    public PropSetting<?> getPropSetting() {
        return propSetting;
    }

    public ColumnInfo setPropSetting(PropSetting<?> propSetting) {
        this.propSetting = propSetting;
        return this;
    }

    public boolean isExport() {
        return export;
    }

    public ColumnInfo setExport(boolean export) {
        this.export = export;
        return this;
    }

    public boolean isExportReadonly() {
        return exportReadonly;
    }

    public ColumnInfo setExportReadonly(boolean exportReadonly) {
        this.exportReadonly = exportReadonly;
        return this;
    }

    public ExportType getExportType() {
        return exportType;
    }

    public ColumnInfo setExportType(ExportType exportType) {
        this.exportType = exportType;
        return this;
    }
}

