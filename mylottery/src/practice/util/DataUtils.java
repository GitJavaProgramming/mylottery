package practice.util;

import base.GLog;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Date;

/**
 * 
 * <pre>
 * 数据转换工具类
 * </pre>
 * 
 * <br>
 * JDK版本:1.6
 * 
 * @author
 * @version 1.0
 * @since 1.0
 */
public class DataUtils {

	/**
	 * 返回指定对象的int值, 如果无法解析成int， 返回0
	 * 
	 * @param obj
	 * @return
	 */
	public static int asInt(Object obj) {
		return asInt(obj, 0);
	}

	/**
	 * 
	 * @param param
	 * @return 字符串
	 */
	public static String asString(Object param) {
		return asString(param, "");
	}

	/**
	 * 返回指定对象的String形式，如果对象为null返回默认值，否则返回对象的toString()方法
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static String asString(Object obj, String defaultValue) {
		String value = defaultValue;
		if (obj instanceof String) {
			value = (String) obj;
		} else if (obj != null) {
			value = obj.toString();
		}
		return value;
	}
	
	/**
     * 返回指定对象的String形式，如果对象为null返回默认值，否则返回对象的toString()方法
     * 
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String asStringExt(Object obj, String defaultValue) {
        String value = defaultValue;
        if (obj instanceof String) {
            value = (String) obj;
        } else if (obj != null) {
            value = obj.toString();
        }
        if (value.endsWith(".0"))
        {
            value = value.substring(0, value.indexOf("."));
        }
        return value;
    }

	/**
	 * 返回指定对象的int值，如果无法解析成int值，返回defaultValue
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static int asInt(Object obj, int defaultValue) {
		
		if (null == obj)
		{
			return defaultValue;
		}
		
		int value = defaultValue;
		if (obj instanceof String) {
			String str = (String) obj;
			if (StringUtils.isNotBlank(str)) {
				try {
					value = Integer.parseInt(str);
				} catch (NumberFormatException e) {
				}
			}
		} else if (obj instanceof Number) {
			value = ((Number) obj).intValue();
		}
		return value;
	}

	/**
	 * 返回指定对象的long值，如果无法解析成long值，返回defaultValue
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static long asLong(Object obj, long defaultValue) {
		long value = defaultValue;
		if (obj instanceof String) {
			String str = (String) obj;
			if (StringUtils.isNotBlank(str)) {
				try {
					value = Long.parseLong(str);
				} catch (NumberFormatException e) {
				}
			}
		} else if (obj instanceof Number) {
			value = ((Number) obj).longValue();
		}
		return value;
	}
	
	/** 求差
	 * @param a
	 * @param b
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static double subDouble(Object a,Object b)
	{
	    return asDouble(a, 0d) - asDouble(b, 0d);
	}
	
	   /** 求和
     * @param a
     * @param b
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static double sumDouble(Object a,Object b)
    {
        return asDouble(a, 0d) + asDouble(b, 0d);
    }
	
	   /** 求差
     * @param a
     * @param b
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int subInt(Object a,Object b)
    {
        return asInt(a, 0) - asInt(b, 0);
    }

	/**
	 * 返回指定对象的long值，如果无法解析成long值，返回0L
	 * 
	 * @param obj
	 * @return
	 */
	public static long asLong(Object obj) {
		return asLong(obj, 0L);
	}

	/**
	 * 返回指定对象的float值，如果无法解析成float值，返回defaultValue
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static float asFloat(Object obj, float defaultValue) {
		float value = defaultValue;
		if (obj instanceof String) {
			String str = (String) obj;
			if (StringUtils.isNotBlank(str)) {
				try {
					value = Float.parseFloat(str);
				} catch (NumberFormatException e) {
				}
			}
		} else if (obj instanceof Number) {
			value = ((Number) obj).floatValue();
		}
		return value;

	}

	/**
	 * 返回指定对象的float值，如果无法解析成float值，返回0f
	 * 
	 * @param obj
	 * @return
	 */
	public static float asFlost(Object obj) {
		return asFloat(obj, 0f);
	}

	/**
	 * 返回指定对象的double值，如果无法解析成double值，返回0.0
	 * 
	 * @param obj
	 * @return
	 */
	public static Object asDouble(Object obj) {
		return asDouble(obj, 0.0);
	}

	/**
	 * 返回指定对象的double值，如果无法解析成double值，返回defaultValue
	 * 
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static double asDouble(Object obj, double defaultValue) {
		double value = defaultValue;
		if (obj instanceof String) {
			String str = (String) obj;
			if (StringUtils.isNotBlank(str)) {
				try {
					value = Double.parseDouble(str);
				} catch (NumberFormatException e) {
				}
			}
		} else if (obj instanceof Number) {
			value = ((Number) obj).doubleValue();
		}
		return value;

	}

	/**
	 * 返回指定对象的boolean值
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean asBoolean(Object obj) {
		return asBoolean(obj, false);
	}

	/**
	 * 
	 * 返回指定对象的boolean值<br>
	 * 示例：DataUtils.asBoolean("TRUE",defaultValue) 返回true<br>
	 * 示例：DataUtils.asBoolean("yes",defaultValue) 返回defaultValue<br>
	 * 示例：DataUtils.asBoolean("False",defaultValue) 返回false<br>
	 * 示例：DataUtils.asBoolean((Integer)1,defaultValue) 返回true<br>
	 * 示例：DataUtils.asBoolean((Integer)0,defaultValue) 返回false<br>
	 * 示例：DataUtils.asBoolean(Boolean.TRUE,defaultValue) 返回true<br>
	 * 
	 * @param obj
	 * @param defaultValue
	 *            如果无法正确转换，返回的默认值
	 * @return
	 */
	public static boolean asBoolean(Object obj, boolean defaultValue) {
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof Number) {
			if (((Number) obj).intValue() == 0)
				return false;
			else
				return true;
		} else if (obj != null) {
			String str = obj.toString();
			if (str.equalsIgnoreCase("true"))
				return true;
			else if (str.equalsIgnoreCase("false"))
				return false;
			else
				return defaultValue;
		} else {
			return defaultValue;
		}
	}

	/**
	 * 返回指定对象的Date值,obj有有效的数据格式为：java.utils.Date,Long和满足yyyy-MM-dd HH:mm:ss的字符串
	 * 
	 * @param obj
	 * @param defaultValue
	 *            如果无法转换，返回的默认值
	 * @return
	 */
	public static Date asDate(Object obj, Date defaultValue) {
		if (obj == null)
			return defaultValue;
		if (obj instanceof Date) {
			return (Date) obj;
		}
		if (obj instanceof Number) {
			return new Date(((Number) obj).longValue());
		}
		String str = obj.toString();
		try {
			return DateUtils.getDate(str, "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			try {
				return DateUtils.getDate(str, "yyyy-MM-dd");
			} catch (ParseException e1) {
                GLog.logger.warn("解析对象[" + obj + "]成时间格式错误");
			}
			return defaultValue;
		}

	}

	/**
	 * 返回指定对象的Date值,obj有有效的数据格式为：java.utils.Date,Long和满足yyyy-MM-dd HH:mm:ss的字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static Date asDate(Object obj) {
		return asDate(obj, null);
	}

	/**
	 * 根据指定的数据类型转换对象
	 * 
	 * @param obj
	 * @param targetClass
	 *            目标数据类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object as(Object obj, Class targetClass) {
		if (targetClass == String.class) {
			return asString(obj);
		}
		if (targetClass == Boolean.class || targetClass == Boolean.TYPE) {
			return asBoolean(obj);
		}
		if (targetClass == Integer.class || targetClass == Integer.TYPE) {
			return asInt(obj);
		}
		if (Date.class == targetClass) {
			return asDate(obj);
		}
		if (targetClass == Double.class || targetClass == Double.TYPE) {
			return asDouble(obj);
		}
		if (targetClass == Float.class || targetClass == Float.TYPE) {
			return asFlost(obj);
		}
		if (targetClass == Long.class || targetClass == Long.TYPE) {
			return asLong(obj);
		}
		if (targetClass.isEnum()) {
			return asEnum(obj, (Class<? extends Enum>) targetClass);
		}
		return obj;
	}

	/**
	 * 返回枚举类型的数据，如果无法转换，返回null
	 * 
	 * @param <T>
	 * @param obj
	 * @param enumClass
	 * @return
	 */
	public static <T extends Enum<T>> T asEnum(Object obj, Class<T> enumClass) {
		if (obj == null)
			return null;
		String name = null;
		int i = -1;
		if (obj instanceof String && StringUtils.isNotBlank((String) obj)) {
			String str = (String) obj;
			if (Character.isDigit(str.charAt(0))) {
				i = asInt(str, -1);
			} else {
				name = str;
			}
		} else {
			i = asInt(obj, -1);
		}
		if (i >= 0) {
			T[] arr = enumClass.getEnumConstants();
			if (i < arr.length) {
				return arr[i];
			} else
				return null;
		} else if (name != null)
			return Enum.valueOf(enumClass, name);
		else
			return null;
	}

	public static int[] asIntArray(Object obj, int defaultValue) {
		if (obj == null)
			return null;
		if (obj.getClass().isArray()) {
			if (obj.getClass().getComponentType() == Integer.TYPE)
				return (int[]) obj;
			else {
				int l = Array.getLength(obj);
				int[] array = new int[l];
				for (int i = 0; i < l; i++) {
					array[i] = asInt(Array.get(obj, i), defaultValue);
				}
				return array;
			}
		} else {
			return new int[] { asInt(obj, defaultValue) };
		}
	}

	public static int[] asIntArray(Object obj) {
		return asIntArray(obj, 0);
	}

	public static double[] asDoubleArray(Object obj, double defaultValue) {
		if (obj == null)
			return null;
		if (obj.getClass().isArray()) {
			if (obj.getClass().getComponentType() == Double.TYPE)
				return (double[]) obj;
			else {
				int l = Array.getLength(obj);
				double[] array = new double[l];
				for (int i = 0; i < l; i++) {
					array[i] = asDouble(Array.get(obj, i), defaultValue);
				}
				return array;
			}
		} else {
			return new double[] { asDouble(obj, defaultValue) };
		}
	}

	public static double[] asDoubleArray(Object obj) {
		return asDoubleArray(obj, 0.0);
	}

	public static long[] asLongArray(Object obj, long defaultValue) {
		if (obj == null)
			return null;
		if (obj.getClass().isArray()) {
			if (obj.getClass().getComponentType() == Long.TYPE)
				return (long[]) obj;
			else {
				int l = Array.getLength(obj);
				long[] array = new long[l];
				for (int i = 0; i < l; i++) {
					array[i] = asLong(Array.get(obj, i), defaultValue);
				}
				return array;
			}
		} else {
			return new long[] { asLong(obj, defaultValue) };
		}
	}

	public static long[] asLongArray(Object obj) {
		return asLongArray(obj, 0l);
	}

	public static float[] asFloatArray(Object obj, float defaultValue) {
		if (obj == null)
			return null;
		if (obj.getClass().isArray()) {
			if (obj.getClass().getComponentType() == Float.TYPE)
				return (float[]) obj;
			else {
				int l = Array.getLength(obj);
				float[] array = new float[l];
				for (int i = 0; i < l; i++) {
					array[i] = asFloat(Array.get(obj, i), defaultValue);
				}
				return array;
			}
		} else {
			return new float[] { asFloat(obj, defaultValue) };
		}
	}

	public static float[] asFloatArray(Object obj) {
		return asFloatArray(obj, 0f);
	}

	public static Date[] asDateArray(Object obj) {
		if (obj == null)
			return null;
		if (obj.getClass().isArray()) {
			if (obj.getClass().getComponentType() == Date.class)
				return (Date[]) obj;
			else {
				int l = Array.getLength(obj);
				Date[] array = new Date[l];
				for (int i = 0; i < l; i++) {
					array[i] = asDate(Array.get(obj, i));
				}
				return array;
			}
		} else {
			return new Date[] { asDate(obj) };
		}
	}

	/**
	 * 从类名称获取对应的Class类型，有效的类名称可以是标准Class.getName()的名称或是java编码习惯上定义类型的文本，<br>
	 * 如以下内容都是有效的typeStr，如果类名称是java.lang.开头的，可以省略java.lang.：<br>
	 * java.lang.String <br>
	 * String <br>
	 * Integer <br>
	 * int <br>
	 * int[] <br>
	 * long[][] <br>
	 * [Ljava.lang.String; (Class名称标准写法,等同String[]) <br>
	 * [[I (Class名称标准写法,等同int[][])
	 * 
	 * @param typeStr
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class toClass(String typeStr) throws ClassNotFoundException {
		Class type;
		if ("boolean".equals(typeStr)) {
			type = Boolean.TYPE;
		} else if ("int".equals(typeStr)) {
			type = Integer.TYPE;
		} else if ("long".equals(typeStr)) {
			type = Long.TYPE;
		} else if ("double".equals(typeStr)) {
			type = Double.TYPE;
		} else if ("float".equals(typeStr)) {
			type = Float.TYPE;
		} else if ("byte".equals(typeStr)) {
			type = Byte.TYPE;
		} else if ("short".equals(typeStr)) {
			type = Short.TYPE;
		} else if ("char".equals(typeStr)) {
			type = Character.TYPE;
		} else {
			int i = typeStr.indexOf("[]");
			String str = typeStr;
			if (i > 0) {
				str = typeStr.substring(0, i);
				int d = 1;
				while ((i = typeStr.indexOf("[]", i + 2)) > 0) {
					d++;
				}
				Class compType = toClass(str);
				type = Array.newInstance(compType, new int[d]).getClass();
			} else {
				try {
					type = Class.forName(str);
				} catch (ClassNotFoundException e) {
					str = "java.lang." + str;
					try {
						type = Class.forName(str);
					} catch (ClassNotFoundException e1) {
						throw e;
					}
				}
			}
		}

		return type;

	}
	
	public static boolean isInteger(String str) {
        int begin = 0;
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        if (str.startsWith("+") || str.startsWith("-")) {
            if (str.length() == 1) {
                return false;
            }
            begin = 1;
        }
        for (int i = begin; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
	
	public static boolean isNumeric(String str) {
        int begin = 0;
        boolean once = true;
        if (str == null || str.trim().equals("")) {
            return false;
        }
        str = str.trim();
        if (str.startsWith("+") || str.startsWith("-")) {
            if (str.length() == 1) {
                return false;
            }
            begin = 1;
        }
        for (int i = begin; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                if (str.charAt(i) == '.' && once) {
                    once = false;
                } else {
                    return false;
                }
            }
        }
        if (str.length() == (begin + 1) && !once) {
            return false;
        }
        return true;
    }
}
