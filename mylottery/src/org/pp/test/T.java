package org.pp.test;

import java.awt.*;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;


public class T
{
    public static void main(String[] args) throws Exception
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTable table = new JTable(20, 4) {
            @Override
            protected JTableHeader createDefaultTableHeader()
            {
                Header header = new Header(20);
                header.add(new HeaderCellLabel("abc", 0, 0, 1, 1), new HeaderCellConstraints(0, 0));
                header.add(new HeaderCellLabel("ss", 0, 0, 1, 1), new HeaderCellConstraints(1, 0));
                header.add(new HeaderCellLabel("s", 0, 0, 1, 1), new HeaderCellConstraints(2, 0));
                header.add(new HeaderCellLabel("dhc", 0, 0, 1, 1), new HeaderCellConstraints(3, 0));
                header.add(new HeaderCellLabel("aaaabbbb", 0, 0, 1, 1), new HeaderCellConstraints(0, 1, 3));
                header.add(new HeaderCellLabel("abc", 0, 0, 1, 1), new HeaderCellConstraints(0, 2, 1, 2));
                header.add(new HeaderCellLabel("ss", 0, 0, 1, 1), new HeaderCellConstraints(1, 2));
                header.add(new HeaderCellLabel("ss", 0, 0, 1, 1), new HeaderCellConstraints(1, 3));
                header.add(new HeaderCellLabel("s", 0, 0, 1, 1), new HeaderCellConstraints(2, 2));
                header.add(new HeaderCellLabel("s", 0, 0, 1, 1), new HeaderCellConstraints(2, 3));
                header.add(new HeaderCellLabel("<html>ss<p>nn<p>b</html>", 0, 0, 1, 1), new HeaderCellConstraints(3, 1, 1, 3));

                return header;
            }
        };
        JScrollPane sp = new JScrollPane(table);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(sp);

        JFrame f = new JFrame();
        f.getContentPane().add(p, BorderLayout.CENTER);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}

class Header extends JTableHeader
{
    private int rowHeight = 20;

    public Header(int rowHeight)
    {
        setLayout(new HeaderLayout());

        this.rowHeight = rowHeight;
    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension d = super.getPreferredSize();
        HeaderLayout layout = (HeaderLayout) getLayout();
        d.height = layout.getRowCount() * rowHeight;

        return d;
    }
}

class HeaderCellLabel extends JLabel
{

    public HeaderCellLabel(Icon icon, int top, int left, int bottom, int right)
    {
        this("", icon, top, left, bottom, right);
    }

    public HeaderCellLabel(String text, Icon icon, int top, int left, int bottom, int right)
    {
        super(text, icon, JLabel.CENTER);

        setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.gray));
    }

    public HeaderCellLabel(String text, int top, int left, int bottom, int right)
    {
        this(text, null, top, left, bottom, right);
    }
}

class HeaderCellConstraints {
    public final int x;
    public final int y;
    public final int colSpan;
    public final int rowSpan;

    public HeaderCellConstraints(final int x, final int y, final int colSpan, final int rowSpan)
    {
        this.x = x;
        this.y = y;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public HeaderCellConstraints(final int x, final int y, final int colSpan)
    {
        this(x, y, colSpan, 1);
    }

    public HeaderCellConstraints(final int x, final int y)
    {
        this(x, y, 1, 1);
    }
}

class HeaderLayout implements LayoutManager2 {
    private HashMap<Component, HeaderCellConstraints> compCellMap =
            new HashMap<Component, HeaderCellConstraints>();

    private int rowCount = 0;

    public void addLayoutComponent(Component comp, Object constraints)
    {
        if (constraints == null || !(constraints instanceof HeaderCellConstraints)) {
            return;
        }

        HeaderCellConstraints cell = (HeaderCellConstraints) constraints;
        compCellMap.put(comp, cell);
        rowCount = Math.max(rowCount, cell.y + cell.rowSpan);
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public float getLayoutAlignmentX(Container target)
    {
        return 0;
    }

    public float getLayoutAlignmentY(Container target)
    {
        return 0;
    }

    public void invalidateLayout(Container target)
    {
    }

    public Dimension maximumLayoutSize(Container target)
    {
        return preferredLayoutSize(target);
    }

    public void addLayoutComponent(String name, Component comp)
    {
        throw new IllegalArgumentException();
    }

    public void layoutContainer(Container parent)
    {
        JTableHeader header = (JTableHeader) parent;
        JTable table = header.getTable();

        TableColumnModel columnModel = table.getColumnModel();
        int rowHeight = header.getHeight() / rowCount;

        Component[] cellComps = parent.getComponents();
        for (Component cellComp : cellComps) {
            HeaderCellConstraints cell = compCellMap.get(cellComp);
            if (cell == null) {
                continue;
            }

            int cellX = 0;
            for (int i = 0; i < cell.x; i++) {
                cellX += columnModel.getColumn(i).getWidth();
            }

            int cellWid = 0;
            for (int i = 0; i < cell.colSpan; i++) {
                cellWid += columnModel.getColumn(cell.x + i).getWidth();
            }

            int cellY = rowHeight * cell.y;
            int cellHei = rowHeight * cell.rowSpan;

            cellComp.setBounds(cellX, cellY, cellWid, cellHei);
        }
    }

    public Dimension minimumLayoutSize(Container parent)
    {
        return preferredLayoutSize(parent);
    }

    public Dimension preferredLayoutSize(Container parent)
    {
        return new Dimension(1,1);
    }

    public void removeLayoutComponent(Component comp)
    {
        compCellMap.remove(comp);
        rowCount = 0;

        for (HeaderCellConstraints cell : compCellMap.values()) {
            rowCount = Math.max(rowCount, cell.y + cell.rowSpan);
        }
    }
}