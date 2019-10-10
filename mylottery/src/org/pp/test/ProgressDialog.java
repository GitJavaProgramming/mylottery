package org.pp.test;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-28
 * Time: 下午1:11
 * To change this template use File | Settings | File Templates.
 */
public class ProgressDialog extends JDialog {

    private JProgressBar progressBar;

    private final int height = 20;//进度条高度
    private final int width = 260;//进度条宽度

    private final int MAX_NUM = 100;


    public ProgressDialog() {
        initialize();
        initProgressBar(MAX_NUM);
        JPanel panel = new JPanel();
        this.setContentPane(panel);
        panel.add(progressBar);
    }

    /**
     * 初始化窗体
     */
    private void initialize() {
//        this.setSize(new Dimension(width, height));
//        this.setAlwaysOnTop(true);
//        this.setModal(true);
        this.setBounds(0, 0, width, height);
        this.setUndecorated(true);
    }

    /**
     * @param number 进度条最大值
     *               初始化进度条
     */
    private void initProgressBar(int number) {
        progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
//        progressBar.setBounds(0, 0, width, height);
        progressBar.setMinimum(0);
        progressBar.setMaximum(number);
        progressBar.setStringPainted(true);//显示进度条数值
        // 添加进度改变通知
        progressBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("当前进度值: " + progressBar.getValue() + "; " + "进度百分比: " + progressBar.getPercentComplete());
            }
        });
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
