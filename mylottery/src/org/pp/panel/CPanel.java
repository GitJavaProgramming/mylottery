package org.pp.panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-28
 * Time: 上午10:21
 * To change this template use File | Settings | File Templates.
 */
public class CPanel extends JPanel {

    private int width;
    private int height;

    public CPanel() {
        Toolkit kit = Toolkit.getDefaultToolkit(); //定义工具包
        Dimension screenSize = kit.getScreenSize(); //获取屏幕的尺寸
        int screenWidth = screenSize.width; //获取屏幕的宽
        int screenHeight = screenSize.height; //获取屏幕的高
        this.setWidth(900);
        this.setHeight(600);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public static void positionCenter(Component component) {
        int windowWidth = component.getWidth(); //获得窗口宽
        int windowHeight = component.getHeight(); //获得窗口高
//        Toolkit kit = Toolkit.getDefaultToolkit(); //定义工具包
//        Dimension screenSize = kit.getScreenSize(); //获取屏幕的尺寸
        Container parent = component.getParent();
        Dimension screenSize = parent.getPreferredSize();
        int screenWidth = screenSize.width; //获取屏幕的宽
        int screenHeight = screenSize.height; //获取屏幕的高
//        System.out.println("高度：" + screenHeight);
//        System.out.println("宽度：" + screenWidth);
        component.setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);//设置窗口居中显示
    }
}
