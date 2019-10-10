package org.pp.filter;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-27
 * Time: 下午4:05
 * To change this template use File | Settings | File Templates.
 */
public class DefaultPipeline implements Pipeline {

    private final LinkedList<ConditionFilter> firstFilterList = new LinkedList<>();

    private final FixedConditionFilter fixedConditionFilter = new FixedConditionFilter();
//    private final DefaultBuildConditionFilter defaultBuildConditionFilter = DefaultBuildConditionFilter.getInstance();

    private static final Pipeline instance = new DefaultPipeline();

    private DefaultPipeline() {
        firstFilterList.addLast(fixedConditionFilter);
//        firstFilterList.addLast(defaultBuildConditionFilter);
    }

    public LinkedList<ConditionFilter> getFilterList() {
        return firstFilterList;
    }

    public static Pipeline getInstance() {
        return instance;
    }
}
