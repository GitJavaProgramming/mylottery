package org.pp.component.table;

/**
 * 限定  表格导出excel时使用
 * */
public interface IQualifier<T> {
	boolean qualify(T obj);
}
