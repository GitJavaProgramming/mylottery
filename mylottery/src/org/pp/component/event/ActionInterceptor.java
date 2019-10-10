package org.pp.component.event;

import java.awt.event.ActionEvent;
import java.util.EventListener;

/**
 * 事件拦截器
 *
 * @author lan
 */
public class ActionInterceptor implements EventListener {

    /**
     * 事件处理前调用此方法
     */
    public void before(ActionEvent event) {
    }

    /**
     * 事件处理后调用此方法
     */
    public void after(ActionEvent event) {
    }

}
