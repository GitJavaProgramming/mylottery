package org.pp.filter;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-27
 * Time: 下午3:25
 * To change this template use File | Settings | File Templates.
 */
public interface Pipeline {
    LinkedList<ConditionFilter> getFilterList();
}
