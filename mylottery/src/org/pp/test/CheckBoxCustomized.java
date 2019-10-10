package org.pp.test;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalCheckBoxUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CheckBoxCustomized extends JDialog {


    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CheckBoxCustomized dialog = new CheckBoxCustomized();
                    dialog.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the dialog
     */
    public CheckBoxCustomized() {
        super();
        getContentPane().setLayout(null);
        setBounds(100, 100, 500, 375);

        final JCheckBox checkBox = new JCheckBox();
        checkBox.setText("New JCheckBox");
        checkBox.setBounds(87, 57, 118, 26);
        getContentPane().add(checkBox);
        //
        MetalCheckBoxUI ui = new MetalCheckBoxUI() {

            public synchronized void paint(Graphics g, JComponent c) {

                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();

                Dimension size = c.getSize();

                Font f = c.getFont();
                g.setFont(f);
                FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, f);

                Rectangle viewRect = new Rectangle(size);
                Rectangle iconRect = new Rectangle();
                Rectangle textRect = new Rectangle();

                Insets i = c.getInsets();
                viewRect.x += i.left;
                viewRect.y += i.top;
                viewRect.width -= (i.right + viewRect.x);
                viewRect.height -= (i.bottom + viewRect.y);

                Icon altIcon = b.getIcon();

                String text = SwingUtilities.layoutCompoundLabel(
                        c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
                        b.getVerticalAlignment(), b.getHorizontalAlignment(),
                        b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                        viewRect, iconRect, textRect, b.getIconTextGap());

                // fill background
                if (c.isOpaque()) {
                    g.setColor(b.getBackground());
                    g.fillRect(0, 0, size.width, size.height);
                }


                // Paint the radio button
                if (altIcon != null) {

                    if (!model.isEnabled()) {
                        if (model.isSelected()) {
                            altIcon = b.getDisabledSelectedIcon();
                        } else {
                            altIcon = b.getDisabledIcon();
                        }
                    } else if (model.isPressed() && model.isArmed()) {
                        altIcon = b.getPressedIcon();
                        if (altIcon == null) {
                            // Use selected icon
                            altIcon = b.getSelectedIcon();
                        }
                    } else if (model.isSelected()) {
                        if (b.isRolloverEnabled() && model.isRollover()) {
                            altIcon = b.getRolloverSelectedIcon();
                            if (altIcon == null) {
                                altIcon = b.getSelectedIcon();
                            }
                        } else {
                            altIcon = b.getSelectedIcon();
                        }
                    } else if (b.isRolloverEnabled() && model.isRollover()) {
                        altIcon = b.getRolloverIcon();
                    }

                    if (altIcon == null) {
                        altIcon = b.getIcon();
                    }

                    altIcon.paintIcon(c, g, iconRect.x, iconRect.y);

                } else {
                    System.out.println("here");
                    // paint the button icon by myself
                    if (model.isEnabled()) {
                        if (model.isPressed() && model.isArmed()) {
                            g.setColor(MetalLookAndFeel.getControlShadow());
                            g.fillRect(iconRect.x, iconRect.y, 13, 13);
                        }
                        g.setColor(c.getForeground());
                    } else {
                        g.setColor(MetalLookAndFeel.getControlShadow());
                    }

                    if (model.isSelected()) {
                        int controlSize = 13;
                        g.fillRect(iconRect.x + 3, iconRect.y + 5, 2,
                                controlSize - 8);
                        g.drawLine(iconRect.x + (controlSize - 4),
                                iconRect.y + 3, iconRect.x + 5, iconRect.y
                                        + (controlSize - 6));
                        g.drawLine(iconRect.x + (controlSize - 4),
                                iconRect.y + 4, iconRect.x + 5, iconRect.y
                                        + (controlSize - 5));
                    }
                }


                // Draw the Text
                if (text != null) {
                    View v = (View) c.getClientProperty(BasicHTML.propertyKey);
                    if (v != null) {
                        v.paint(g, textRect);
                    } else {
                        int mnemIndex = b.getDisplayedMnemonicIndex();
                        if (model.isEnabled()) {
                            // *** paint the text normally
                            g.setColor(b.getForeground());
                        } else {
                            // *** paint the text disabled
                            g.setColor(getDisabledTextColor());
                        }
                        SwingUtilities2.drawStringUnderlineCharAt(c, g, text,
                                mnemIndex, textRect.x, textRect.y + fm.getAscent());
                    }
                    if (b.hasFocus() && b.isFocusPainted() &&
                            textRect.width > 0 && textRect.height > 0) {
                        paintFocus(g, textRect, size);
                    }
                }
            }
        };

        checkBox.setUI(ui);
        System.out.println(checkBox.isFocusPainted());
        checkBox.setFocusPainted(false); // this is optional
        System.out.println();
    }

}