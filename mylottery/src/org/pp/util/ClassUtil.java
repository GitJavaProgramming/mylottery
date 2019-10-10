package org.pp.util;

import practice.util.DataUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ClassUtil {

    public static Object doInvokeGetter(Field field, Object obj) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(obj);
    }

    public static void doInvokeSetter(Field field, Object obj, Object value) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        Object v = DataUtils.as(value, field.getType());
        field.set(obj, v);
    }

    public static Class<?> getActualTypeArgument(Type type) {
        return getActualTypeArgument(type, 0);
    }

    public static Class<?> getActualTypeArgument(Type type, int index) {
        Type t = type;
        while (t instanceof Class && !(t instanceof ParameterizedType))
            t = ((Class<?>) t).getGenericSuperclass();
        return (Class<?>) ((ParameterizedType) t).getActualTypeArguments()[index];
    }

    public static boolean safeEquals(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    /**
     * 判断parent类型是否child类型的超类.如果parent和child类型相同,也会返回true.
     * 如果parent类型是child类型的包装类型,也会返回true
     *
     * @param parent
     * @param child
     * @return
     */
    public static boolean isSuper(Class<?> parent, Class<?> child) {
        if (parent == child) {
            return true;
        }
        if (child.isPrimitive()) {
            // 基本类型,boolean、byte、char、short、int、long、float 和 double。
            // 其实可以==的,因为所有基本类型的包装类型都是final的
            if (child == boolean.class) {
                return Boolean.class.isAssignableFrom(parent);
            } else if (child == byte.class) {
                return Byte.class.isAssignableFrom(parent);
            } else if (child == char.class) {
                return Character.class.isAssignableFrom(parent);
            } else if (child == short.class) {
                return Short.class.isAssignableFrom(parent);
            } else if (child == int.class) {
                return Integer.class.isAssignableFrom(parent);
            } else if (child == long.class) {
                return Long.class.isAssignableFrom(parent);
            } else if (child == float.class) {
                return Float.class.isAssignableFrom(parent);
            } else if (child == double.class) {
                return Double.class.isAssignableFrom(parent);
            }
        }
        return parent.isAssignableFrom(child);
    }

    public static void main(String[] args) {
        System.out.println(isSuper(Integer.class, int.class));
        System.out.println(isSuper(int.class, Integer.class));
    }
}
