package org.pp.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class RoseJFrame extends JFrame implements ActionListener, ComponentListener {
    private RoseCanvas canvas;                             //自定义画布组件
    private JButton button_color;                          //选择颜色按钮

    public RoseJFrame() {
        super("四叶玫瑰线");                                //框架边布局
        Dimension dim = getToolkit().getScreenSize();        //获得屏幕分辨率
        this.setBounds(dim.width / 4, dim.height / 4, dim.width / 2, dim.height / 2);  //窗口居中
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addComponentListener(this);                   //注册组件事件监听器

        JPanel jpanel = new JPanel();                      //面板流布局，居中
        this.getContentPane().add(jpanel, "North");
        button_color = new JButton("选择颜色");
        jpanel.add(button_color);
        button_color.addActionListener(this);

        canvas = new RoseCanvas(Color.red);                //创建自定义画布组件
        this.getContentPane().add(canvas, "Center");
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)             //按钮单击事件处理方法
    {
        Color c = JColorChooser.showDialog(this, "选择颜色", Color.blue); //弹出JColorChooser颜色选择对话框，返回选中颜色
        canvas.setColor(c);
        canvas.repaint();                                  //重画
    }

    public void componentResized(ComponentEvent e)         //改变组件大小时
    {
        this.repaint(); //重画
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public static void main(String arg[]) {
        new RoseJFrame();
    }
}

class RoseCanvas extends Canvas                            //画布组件
{
    private Color color;                                   //颜色

    public RoseCanvas(Color color) {
        this.setColor(color);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void paint(Graphics g)                          //在Canvas上作图
    {
        int x0 = this.getWidth() / 2;                        //(x0,y0)是组件正中点坐标
        int y0 = this.getHeight() / 2;
        g.setColor(color);                                 //设置画线颜色
        g.drawLine(x0, 0, x0, y0 * 2);                          //画X轴
        g.drawLine(0, y0, x0 * 2, y0);                          //画Y轴
        for (int j = 40; j < 200; j += 20)                       //画若干圈四叶玫瑰线
            for (int i = 0; i < 1023; i++)                     //画一圈四叶玫瑰线的若干点
            {
                double angle = i * Math.PI / 512;
                double radius = j * Math.sin(2 * angle);            //纯数学理论
                int x = (int) Math.round(radius * Math.cos(angle) * 2);
                int y = (int) Math.round(radius * Math.sin(angle));
                g.fillOval(x0 + x, y0 + y, 1, 1);                 //画直径为1的圆就是一个点
            }
    }
}