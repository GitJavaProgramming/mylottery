package org.pp.util;

import org.apache.poi.hssf.usermodel.*;
import org.pp.component.CProgressBar;
import org.pp.component.table.CGridTable;
import org.pp.component.table.ColumnInfo;
import org.pp.component.table.IQualifier;
import org.pp.component.table.cell.cellRenderer.CTableCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ExcelTxtUtil {

    private static final String saveFileExtension = ".xls";
    private static final String saveFileDescription = "Excel文件(*.xls)";

    private static Map<ColumnInfo.ExportType, HSSFCellStyle> lockedStyles;
    private static Map<ColumnInfo.ExportType, HSSFCellStyle> normalStyles;

    /**
     * @param table
     * @param dlgParent      文件选择窗口及信息窗口的parent，默认是table
     * @param reQueryAllData 是否重新查出所有数据，如果传true（默认），会调getList()重查数据，如果传false，只会导出表格当前页的数据
     * @param showMsgDialog  是否显示出错信息窗口及成功信息窗口，默认是true
     * @return
     */
    public static <T> boolean exportTable(CGridTable<T> table, Component dlgParent, boolean reQueryAllData, boolean showMsgDialog, IQualifier<T> qualifier) {
        try {
            File file = FileUtil.getSaveFile(dlgParent, saveFileDescription, saveFileExtension);
            if (file == null) {
                return false;
            }
            boolean rt = exportTable(table, file, reQueryAllData, qualifier);

            if (showMsgDialog) {
                JOptionPane.showMessageDialog(dlgParent, "成功导出表格。");
            }
            return rt;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dlgParent, "另一个程序正在使用此文件，进程无法访问。");
        }
        return false;
    }

    /**
     * 导出表格
     *
     * @param table
     * @param file
     * @param reQueryAllData 是否重新查出所有数据，如果传true（默认），会调getList()重查数据，如果传false，只会导出表格当前页的数据
     * @throws java.io.IOException
     */
    public static <T> boolean exportTable(final CGridTable<T> table, File file, final boolean reQueryAllData, final IQualifier<T> qualifier) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        final FileOutputStream out = new FileOutputStream(file);
        final HSSFWorkbook wb = new HSSFWorkbook();
        final HSSFSheet sheet = wb.createSheet();
        lockedStyles = new HashMap<ColumnInfo.ExportType, HSSFCellStyle>();
        normalStyles = new HashMap<ColumnInfo.ExportType, HSSFCellStyle>();

        try {
            exportTableHeader(table, wb, sheet);

            final java.util.List<T> data = reQueryAllData ? table.getData() : table.getData();
            final int rowCount = reQueryAllData ? data.size() : table.getRowCount();

            final CProgressBar.CProgressDialog dlg = new CProgressBar.CProgressDialog(table, 0, rowCount);

            final Thread exportThread = new Thread() {
                @Override
                public void run() {
                    try {
                        int colIndex = 0;
                        for (int index = 0, rowIndex = 1; index < rowCount; ++index) {
                            if (qualifier != null && !qualifier.qualify(data.get(index))) {
                                continue;
                            }
                            HSSFRow row = sheet.createRow(rowIndex++);
                            colIndex = 0;
                            for (int j = 0; j < table.getVisibleColumns().length; ++j) {
                                ColumnInfo c = table.getVisibleColumnAt(j);
                                if (!c.isExport()) {
                                    continue;
                                }
                                Object value = null;
                                if (reQueryAllData) {
                                    try {
                                        if (c.isUserColumn()) {
                                            if (c.isIndexColumn()) {
                                                value = index + 1;
                                            } else if (c.isSelectColumn()) {
                                                value = table.isRowChecked(data.get(index));
                                            }
                                        } else {
                                            T t = data.get(index);
                                            String name = c.getColName();
                                            Class<?> clazz = t.getClass();
                                            Field field = clazz.getDeclaredField(name);
                                            value = ClassUtil.doInvokeGetter(field, t);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                writeCell(table, wb, row.createCell(colIndex++), reQueryAllData ? value : table.getValueAt(index, j), reQueryAllData ? 0 : index, j);
                            }
                            dlg.setProgressValue(index);
//                            Thread.sleep(200);
                        }
                        for (short i = 0; i < colIndex; ++i) {
                            sheet.setColumnWidth(i, 4000);
                        }
                        sheet.protectSheet("");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(SwingUtil.getParentWindow(table), "导出被取消。");
                        dlg.setCanceled(true);
                        e.printStackTrace();
                    } finally {
                        dlg.dispose();
                    }
                }
            };
            exportThread.setDaemon(true);
            dlg.setRelateThread(exportThread);
            exportThread.start();
            dlg.setVisible(true);
            if (dlg.isCanceled()) {
                file.delete();
            } else {
                wb.write(out);
                out.flush();
            }
            return !dlg.isCanceled();
        } finally {
            out.close();
        }
    }

    private static <T> void writeCell(CGridTable<T> table, HSSFWorkbook wb, HSSFCell cell, Object value, int row, int column) {
        ColumnInfo c = table.getVisibleColumnAt(column);
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        String text;
        if (renderer != null && renderer instanceof CTableCellRenderer && ((CTableCellRenderer) renderer).getComponent() != null) {
            text = ((CTableCellRenderer) renderer).getComponent().getTextByValue(value);
        } else if (renderer != null && renderer instanceof DefaultTableCellRenderer) {
            renderer.getTableCellRendererComponent(table, value, false, false, row, column);
            text = ((DefaultTableCellRenderer) renderer).getText();
        } else {
            text = value == null ? "" : value.toString();
        }
        cell.setCellValue(text);
        cell.setCellStyle(getStyle(wb, c.getExportType(), c.isExportReadonly()));
    }


    public static HSSFCellStyle getStyle(HSSFWorkbook wb, ColumnInfo.ExportType type, boolean locked) {
        return locked ? getLockedStyle(wb, type) : getNormalStyle(wb, type);
    }

    public static HSSFCellStyle getLockedStyle(HSSFWorkbook wb, ColumnInfo.ExportType type) {
        HSSFCellStyle style = lockedStyles.get(type);
        if (style != null)
            return style;
        style = wb.createCellStyle();
        style.setLocked(true);
        switch (type) {
            case Number:
                style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
                break;
            default:
                style.setDataFormat(HSSFDataFormat.getBuiltinFormat("TEXT"));
        }
        lockedStyles.put(type, style);
        return style;
    }

    public static HSSFCellStyle getNormalStyle(HSSFWorkbook wb, ColumnInfo.ExportType type) {
        HSSFCellStyle style = normalStyles.get(type);
        if (style != null)
            return style;
        style = wb.createCellStyle();
        style.setLocked(false);
        switch (type) {
            case Number:
                style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
                break;
            default:
                style.setDataFormat(HSSFDataFormat.getBuiltinFormat("TEXT"));
        }
        normalStyles.put(type, style);
        return style;
    }

    private static void exportTableHeader(CGridTable<?> table, HSSFWorkbook wb, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        for (int index = 0, colIndex = 0; index < table.getVisibleColumns().length; ++index) {
            if (table.getVisibleColumnAt(index).isExport()) {
                HSSFCell cell = row.createCell(colIndex++);
                HSSFCellStyle style = wb.createCellStyle();
                style.setLocked(true);
                cell.setCellStyle(style);
                cell.setCellValue(table.getVisibleColumnAt(index).getText());
                //sheet.addCell(new Label(colIndex++, 0, table.getVisibleColumnAt(index).getText(), format));
            }
        }
    }
}
