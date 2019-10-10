package org.pp.component;

import javax.swing.border.LineBorder;
import java.awt.*;

public class CRoundedLineBorder extends LineBorder {

    public CRoundedLineBorder(Color color) {
        this(color, 1);
    }

    public CRoundedLineBorder(Color color, int thickness) {
        this(color, thickness, 8);
    }

    public CRoundedLineBorder(Color color, int thickness, int cornerWidth) {
        this(color, Color.gray, thickness, cornerWidth);
    }

    public CRoundedLineBorder(Color color, Color shadow) {
        this(color, shadow, 1, 8);
    }

    public CRoundedLineBorder(Color color, Color shadow, int thickness, int cornerWidth) {
        super(color, thickness);
        this.cornerWidth = cornerWidth;
        this.shadow = shadow;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int i;

        if (shadow != null) {
            g.setColor(shadow);
            g.drawRoundRect(x + thickness, y + thickness, width - thickness * 2, height - thickness * 2,
                    cornerWidth, cornerWidth);
            g.setColor(shadow.brighter());
            g.drawRoundRect(x + thickness + 1, y + thickness + 1, width - thickness * 2 - 2,
                    height - thickness * 2 - 2, cornerWidth, cornerWidth);
        }

        g.setColor(lineColor);
        for (i = 0; i < thickness; i++) {
            g.drawRoundRect(x + i, y + i, width - i - i - 1, height - i - i - 1, cornerWidth, cornerWidth);
        }

        g.setColor(oldColor);
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(thickness + 2, thickness + 2 + cornerWidth / 2, thickness + 2, thickness + 2 + cornerWidth / 2);
    }

    protected int cornerWidth;
    protected Color shadow;
}
