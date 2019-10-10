package org.pp.util;

import java.lang.reflect.Array;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-4-5
 * Time: 下午6:05
 * To change this template use File | Settings | File Templates.
 */
public class ArrayUtil {
    public static <T> T[] arrayAppend(T[] array, T... items) {
        int len = array == null ? 0 : array.length, appendLen = items == null ? 0 : items.length;
        if (len == 0 && appendLen == 0)
            return null;
        Class<?> cls = len > 0 ? array[0].getClass() : items[0].getClass();
        T[] arr = (T[]) Array.newInstance(cls, len + appendLen);
        if (len > 0)
            System.arraycopy(array, 0, arr, 0, len);
        if (appendLen > 0)
            System.arraycopy(items, 0, arr, len, appendLen);
        return arr;
    }
}
