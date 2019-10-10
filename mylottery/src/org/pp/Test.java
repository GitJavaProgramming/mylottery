package org.pp;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.pp.component.table.cell.cellCombo.NameValue;
import org.pp.component.table.cell.cellCombo.CListCellComponent;
import org.pp.component.table.CGridTable;
import org.pp.component.table.CellAlignEnum;
import org.pp.component.table.ColumnInfo;
import org.pp.component.table.PropSetting;
import org.pp.component.table.cell.cellEditor.CTableCellEditor;
import org.pp.handler.WindowEventHandler;
import org.pp.test.CheckBoxColumn;
import org.pp.test.LabelColumn;
import org.pp.test.ProgressColumn;
import org.pp.test.ProgressDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-25
 * Time: 下午11:27
 * To change this template use File | Settings | File Templates.
 */
public class Test {


    private static ProgressDialog progressDialog = new ProgressDialog();

    private static int rate = 0;

    public static void main(String[] args) {
        /**/

        System.out.println(false & true);

        Integer.class.isAssignableFrom(int.class);
        System.out.println("Integer.class.isPrimitive()=" + Integer.class.isPrimitive());

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        for (String font : fonts) {
//            System.out.println(font);
        }


        //设置皮肤，搜索关键字beautyeye下载相关的jar包
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();

            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
        }


//        System.out.println("01,08,9-01,2,4".matches("^((\\d+),)*(\\d+)-((\\d+),)*(\\d+)$"));
//        saveFile();

//        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (InstantiationException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (UnsupportedLookAndFeelException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        showFrame();
    }

    private static JScrollPane createTable() {
        String[] columnHeaders = {"书名", "库存", "单价", "类型", "评分", "入库日期", "出版社", "联系电话"};
        DefaultTableModel tableModel = new DefaultTableModel();
        // 创建表格头部字段
        tableModel.addColumn("");
        for (String header : columnHeaders)
            tableModel.addColumn(header);
        tableModel.addColumn("操作");

        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 表格选择为单选
        table.setRowHeight(25); // 设置行高
        table.setShowVerticalLines(false);// 使表格的列线条不显示
        // 使第一列的大小固定不变
        table.getColumn("").setPreferredWidth(55);
        table.getColumn("").setMaxWidth(55);
        table.getColumn("").setMinWidth(55);
        // 设置表格排序，表格默认将所有值都作为字符串进行排序，
        // 所以对于表格中已有字符型列可以不用自定义排序类
        // 但需要对非字符串的列进行自定义排序
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        sorter.setComparator(2, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        // 对单价列进行排序，由于单位已经被格式化了货币字符串
        // 所以要对字符串进行转换后再比较
        sorter.setComparator(3, new Comparator<String>() {
            public int compare(String o1, String o2) {
                BigDecimal b1 = new BigDecimal(o1.substring(1));
                BigDecimal b2 = new BigDecimal(o2.substring(1));
                return b1.compareTo(b2);
            }
        });
        sorter.setComparator(6, new Comparator<Date>() {
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });
        table.setRowSorter(sorter);
        // 设置各列的呈现方式
        new CheckBoxColumn(table.getColumn(""));
        new LabelColumn(table.getColumn("书名"));
        new LabelColumn(table.getColumn("单价"), null, Color.BLUE, SwingConstants.RIGHT);
        new ProgressColumn(table.getColumn("库存"), 100);
        new LabelColumn(table.getColumn("评分"), SwingConstants.CENTER);
        new LabelColumn(table.getColumn("类型"));
        new LabelColumn(table.getColumn("入库日期"), SwingConstants.LEFT);
        new LabelColumn(table.getColumn("出版社"), SwingConstants.CENTER);
//        new ComboBoxColumn(table.getColumn("联系电话"));
//        new ButtonColumn(table.getColumn("操作"), "编辑", center);
        return new JScrollPane(table);
    }

    static Vector<Integer> vector;

    {
        vector = new Vector<Integer>();
        vector.add(1);
        vector.add(2);
        vector.add(3);
        vector.add(4);
        vector.add(5);
        vector.add(6);
        vector.add(7);
        vector.add(8);
    }

//    public void updateTable() {
//        // 清空表格模型内的数据
//        tableModel.getDataVector().clear();
//        tableModel.fireTableDataChanged();
//        new SwingWorker<List<BookView>, Vector>() {
//            protected List<BookView> doInBackground() throws Exception {
//                // 得到书籍的总数
//                bookTotal = center.getDataManager().getBookTotal();
//                // 根据当前的页数的返回一个结果集
//                List<BookView> books = center.getDataManager().getBookViewByPage(pageSize, pageNo);
//                for (BookView book : books) {
//                    Vector rowData = new Vector();
//                    rowData.add(new Boolean(false));
//                    rowData.add(book.getBookName());
//                    rowData.add(book.getStorage());
//                    rowData.add(NumberFormat.getCurrencyInstance().format(book.getPrice().doubleValue()));
//                    rowData.add(book.getTypeName());
//                    rowData.add(book.getGrade());
//                    rowData.add(book.getStorageDate());
//                    rowData.add(book.getPublishName());
//                    rowData.add(book.getPublishPhone());
//                    rowData.add(book.getId());
//                    this.publish(rowData);
//                }
//                return books;
//            }
//
//            protected void process(List<Vector> chunks) {
//                lblStatueBar.setText(StringHelper.LOADING);
//                for (Vector rowData : chunks) {
//                    tableModel.addRow(rowData);
//                }
//            }
//
//            protected void done() {
//                lblStatueBar.setText("共有" + bookTotal + "条书籍记录！");
//                pageTotal = bookTotal / pageSize;
//                pageTotal += bookTotal / pageSize > 0 ? 1 : 0;
//                cboPage.removeAllItems();
//                for (int p = 1; p <= pageTotal; p++)
//                    cboPage.addItem(p);
//                cboPage.setSelectedItem(pageNo);
//                chkDeleteAll.setSelected(false);
//            }
//
//            ;
//        }.execute();
//    }

    public static void showFrame() {
        final JFrame frame = new JFrame();
        frame.setTitle("大乐透分析");
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
//        panel.setSize(new Dimension(900, 400));
        frame.setContentPane(panel);

        ColumnInfo c1 = new ColumnInfo("name", "pName").setWidth(20).setEditable(true);
        PropSetting<Person> propSetting = new PropSetting<Person>() {
            @Override
            public Color getBackground(Person row, boolean selected) {
                return Color.YELLOW;
            }

            @Override
            public Color getForeground(Person row) {
                return Color.BLACK;
            }
        };
        ColumnInfo c2 = new ColumnInfo("age", "pAge").setWidth(20).setEditable(true).setAlign(CellAlignEnum.center).setPropSetting(propSetting);
        List<String> vector = new ArrayList<>();
        vector.add("11");
        vector.add("22");
        vector.add("33");
        vector.add("44");
        java.util.List<Person> personList = new ArrayList<>();
        personList.add(new Person(1, "11"));
        personList.add(new Person(2, "22"));
        personList.add(new Person(3, "33"));
        personList.add(new Person(4, "44"));

        java.util.List<NameValue> nameValueList = new ArrayList<>();
        nameValueList.add(new NameValue("11", 11));
        nameValueList.add(new NameValue("22", 22));
        nameValueList.add(new NameValue("33", 33));
        nameValueList.add(new NameValue("44", 44));

        final CGridTable<Person> table = new CGridTable<Person>(/*ColumnInfo.createSelectColumn(), c1*/);

//        c2.setEditor(CComboCellComponent.createEditor(nameValueList));
//        c2.setRenderer(CComboCellComponent.createRenderer(nameValueList));
//        c2.setEditor(new CTableCellComboEditor(new CComboCellComponent(nameValueList)).setShowInCombo(table));
//        c1.setRenderer(new CTableCellRenderer(new CTextCellComponent()).setShowComponent(true));
        c1.setEditor(new CTableCellEditor(new CListCellComponent(vector)).setShowInCombo(table));
//        c1.setEditor(new CTableCellEditor(new CPanelCellComponent()).setShowInCombo(table));
        table.addColumn(c1, 100);
//        table.setRowPropSetting(propSetting);
        table.setData(personList);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setViewportView(table);

        JPanel panel1 = new JPanel();
        JButton btn = new JButton("导出");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(table.getCheckedRows());
//                table.deleteEntry();
//                table.addEntry(new Person(9, "99"));
//                table.insertFrontOfEntry(new Person(9, "99"));
//                table.insertBackOfEntry(new Person(9, "99"));
//                System.out.println(table.getData());
//                table.unChecked();
//                System.out.println(table.getCheckedRows());
                table.exportExcel();
            }
        });
        JButton btn2 = new JButton("向后");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(table.getCheckedRows());
//                table.deleteEntry();
//                table.addEntry(new Person(9, "99"));
//                table.insertFrontOfEntry(new Person(9, "99"));
                table.insertBackOfEntry(new Person(111, "111"));
//                System.out.println(table.getData());
//                table.unChecked();
//                System.out.println(table.getCheckedRows());
            }
        });
        panel1.add(btn);
        panel1.add(btn2);
        frame.getContentPane().add(panel1, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

//        frame.getContentPane().add(createTable());

        frame.pack();
//        frame.setResizable(false);
        frame.addWindowListener(new WindowEventHandler());

//        final CProgressBar.CProgressDialog dlg = new CProgressBar.CProgressDialog(frame, 0, 100);
//        frame.addMouseWheelListener(new MouseAdapter() {
//            @Override
//            public void mouseWheelMoved(MouseWheelEvent e) {
//                int rotation = e.getWheelRotation();
//                if (rotation == 1) {
//                    dlg.setProgressValue(rate++);
//                } else {
//                    dlg.setProgressValue(rate--);
//                }
//                dlg.setVisible(true);
//            }
//        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class Person {
        private String name;
        private int age;

        Person() {
        }

        Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        int getAge() {
            return age;
        }

        void setAge(int age) {
            this.age = age;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static void saveFile() {
        //弹出文件选择框
        JFileChooser chooser = new JFileChooser();

        //下面的方法将阻塞，直到【用户按下保存按钮且“文件名”文本框不为空】或【用户按下取消按钮】
        int option = chooser.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {    //假如用户选择了保存
            File file = chooser.getSelectedFile();
            try {
                FileOutputStream fos = new FileOutputStream(file);

                //写文件操作……

                fos.close();

            } catch (IOException e) {
                System.err.println("IO异常");
                e.printStackTrace();
            }
        }
    }

    public static void fileChooser() {

        Locale.setDefault(Locale.ENGLISH);//设置语言

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt");
        // 设置文件类型
        chooser.setFileFilter(filter);

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//设置保存路径
        //chooser.showOpenDialog(null);//设置是否先点击打开在保存

        // 打开选择器面板
        int returnVal = chooser.showSaveDialog(new JPanel());
        // 保存文件从这里入手，输出的是文件名
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("你打开的文件夹是: " + chooser.getSelectedFile().getPath());
            String path = chooser.getSelectedFile().getPath();
            System.out.println("path:" + path);
            try {
                File f = new File(path + "\\" + ".csv");
                System.out.println(f.getAbsolutePath());
                f.createNewFile();
                FileOutputStream out = new FileOutputStream(f);

                out.write("测试样例".getBytes());
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
