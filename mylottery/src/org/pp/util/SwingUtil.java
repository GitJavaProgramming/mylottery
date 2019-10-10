package org.pp.util;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-4-2
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class SwingUtil {

    public static Window getParentWindow(Component c) {
        while (c != null && !(c instanceof Window))
            c = c.getParent();

        return (Window) c;
    }
}
