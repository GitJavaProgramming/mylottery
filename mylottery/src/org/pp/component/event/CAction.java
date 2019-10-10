package org.pp.component.event;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;

public abstract class CAction extends AbstractAction {

    private EventListenerList list;

    public CAction() {
        super();
    }

    public CAction(String name, Icon icon) {
        super(name, icon);
    }

    public CAction(String name) {
        super(name);
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        if (list != null) {
            for (ActionInterceptor ai : list.getListeners(ActionInterceptor.class)) {
                ai.before(e);
            }
        }

        this.doAction(e);

        if (list != null) {
            for (ActionInterceptor ai : list
                    .getListeners(ActionInterceptor.class)) {
                ai.after(e);
            }
        }
    }

    /**
     * 事件处理
     *
     * @param e
     */
    protected abstract void doAction(ActionEvent e);

    /**
     * 添加事件拦截器
     */
    public void addInterceptor(ActionInterceptor interceptor) {
        if (list == null) {
            list = new EventListenerList();
        }
        list.add(ActionInterceptor.class, interceptor);
    }

    /**
     * 删除事件拦截器
     *
     * @param interceptor
     */
    public void removeInterceptor(ActionInterceptor interceptor) {
        if (list == null) {
            return;
        }
        list.remove(ActionInterceptor.class, interceptor);
    }
}
