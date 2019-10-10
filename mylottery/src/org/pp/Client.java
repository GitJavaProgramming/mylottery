package org.pp;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.pp.filter.DefaultBuildConditionFilter;
import org.pp.spider.FetchUtil;
import org.pp.task.CacheSelectionNumberTask;
import org.pp.util.ConfigUtil;
import org.pp.util.NumberUtil;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-25
 * Time: 下午9:12
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    public static void main(String[] args) {
        DefaultBuildConditionFilter.getInstance();
        try {
//            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
//            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
//            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            BeautyEyeLNFHelper.translucencyAtFrameInactive = true;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });

        CacheSelectionNumberTask.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行钩子，请检查文件是否已经写入完成等操作。");
            }
        }));
    }
}
