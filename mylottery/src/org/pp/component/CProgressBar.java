package org.pp.component;

import com.sun.awt.AWTUtilities;
import org.pp.component.event.CAction;
import org.pp.util.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-4-2
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class CProgressBar extends JProgressBar {

    public static class CProgressDialog extends JDialog {
        public CProgressDialog(Component owner, int min, int max) {
            super(SwingUtil.getParentWindow(owner));
            setModal(true);
//            setAlwaysOnTop(true);
//            setSize(1, 1);

            setUndecorated(true);
            AWTUtilities.setWindowOpaque(this, true);
            getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(new CRoundedLineBorder(SystemColor.desktop));
            setContentPane(panel);

            progressBar = new CProgressBar(min, max);
            getContentPane().add(progressBar, BorderLayout.CENTER);

            JPanel pnl = new JPanel();
            pnl.setLayout(new FlowLayout(FlowLayout.CENTER));

            pnl.add(getBtnCancel());
            getContentPane().add(pnl, BorderLayout.SOUTH);

            pack();
        }

        public void setProgressValue(int n) {
            progressBar.setValue(n);
        }

        private JButton getBtnCancel() {
            if (btnCancel == null) {
                btnCancel = new JButton(new CAction("取消(ESC)") {

                    @Override
                    protected void doAction(ActionEvent e) {
                        if (JOptionPane.showConfirmDialog(CProgressDialog.this, "取消操作？", "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            if (relateThread != null) {
                                relateThread.interrupt();
                            }
                            CProgressDialog.this.dispose();
                            canceled = true;
                        }
                    }
                });
                btnCancel.setSize(new Dimension(80, 22));
            }

            return btnCancel;
        }

        public void setRelateThread(Thread thread) {
            relateThread = thread;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }

        private CProgressBar progressBar;
        private JButton btnCancel;
        private boolean canceled = false;
        private Thread relateThread;
    }

    public CProgressBar() {
        super();
    }

    public CProgressBar(int min, int max) {
        super(min, max);
        setStringPainted(true);//显示进度条数值
    }
}
