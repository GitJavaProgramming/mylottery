package practice.util;

import org.apache.commons.beanutils.*;
import practice.util.exception.BeanAccessException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintStream;
import java.lang.reflect.*;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <pre></pre>
 * 
 * <br>
 * JDK版本:1.6
 * 
 * @author 刘俊杰
 * @version 1.0
 * @since 1.0
 */
public class BeanUtils {

    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
    }

    /**
     * 获取某个类属性
     * @param clazz
     * @param name
     * @return
     */
	public static Object getStaticProperty(Class clazz, String name) {
		try {
			return clazz.getField(name).get(null);
		} catch (IllegalArgumentException e) {
			throw new BeanAccessException("在读取类[" + clazz + "]的静态属性[" + name
					+ "]时遇到错误", e);
		} catch (SecurityException e) {
			throw new BeanAccessException("在读取类[" + clazz + "]的静态属性[" + name
					+ "]时遇到错误", e);
		} catch (IllegalAccessException e) {
			throw new BeanAccessException("在读取类[" + clazz + "]的静态属性[" + name
					+ "]时遇到错误", e);
		} catch (NoSuchFieldException e) {
			throw new BeanAccessException("在读取类[" + clazz + "]的静态属性[" + name
					+ "]时遇到错误", e);
		}
	}

    /**
     * 获取实例属性
     * @param bean
     * @param name
     * @return
     */
	public static Object getProperty(Object bean, String name) {
		try {
			return PropertyUtils.getProperty(bean, name);
		} catch (NestedNullException e) {
			return null;
		} catch (IllegalAccessException e) {
			throw new BeanAccessException("在读取bean[" + bean + "]的属性[" + name
					+ "]时遇到错误", e);
		} catch (InvocationTargetException e) {
			throw new BeanAccessException("在读取bean[" + bean + "]的属性[" + name
					+ "]时遇到错误", e);
		} catch (NoSuchMethodException e) {
			throw new BeanAccessException("在读取bean[" + bean + "]的属性[" + name
					+ "]时遇到错误", e);
		}
	}

    /**
     * 设置属性值
     * @param bean
     * @param name
     * @param value
     */
	public static void setProperty(Object bean, String name, Object value) {
		try {
			PropertyUtils.setProperty(bean, name, value);
		} catch (IllegalAccessException e) {
			throw new BeanAccessException("在将属性[" + name + "]写入bean[" + bean
					+ "]时遇到错误", e);
		} catch (InvocationTargetException e) {
			throw new BeanAccessException("在将属性[" + name + "]写入bean[" + bean
					+ "]时遇到错误", e);
		} catch (NoSuchMethodException e) {
			throw new BeanAccessException("在将属性[" + name + "]写入bean[" + bean
					+ "]时遇到错误", e);
		}
	}

    /**
     * 设置基本类型属性值
     * @param bean
     * @param name
     * @param value
     */
	public static void setSimpleProperty(Object bean, String name, Object value) {
		try {
			PropertyUtils.setSimpleProperty(bean, name, value);
		} catch (IllegalAccessException e) {
			throw new BeanAccessException("在将属性[" + name + "]写入bean[" + bean
					+ "]时遇到错误", e);
		} catch (InvocationTargetException e) {
			throw new BeanAccessException("在将属性[" + name + "]写入bean[" + bean
					+ "]时遇到错误", e);
		} catch (NoSuchMethodException e) {
			throw new BeanAccessException("在将属性[" + name + "]写入bean[" + bean
					+ "]时遇到错误", e);
		}
	}

	/**
	 * 获取bean的指定名称的属性，如果不存在指定的属性，返回null
	 * 
	 * @param bean
	 * @param name
	 * @return
	 */
	public static Object getPropertyIf(Object bean, String name) {
		if (hasProperty(bean, name)) {
			return getProperty(bean, name);
		} else
			return null;
	}

    /**
     * 获取bean的属性
     * @param bean
     * @param name
     * @return
     */
	public static Object getSimpleProperty(Object bean, String name) {
		try {
			return PropertyUtils.getSimpleProperty(bean, name);
		} catch (IllegalAccessException e) {
			throw new BeanAccessException("在读取bean[" + bean + "]的属性[" + name
					+ "]时遇到错误", e);
		} catch (InvocationTargetException e) {
			throw new BeanAccessException("在读取bean[" + bean + "]的属性[" + name
					+ "]时遇到错误", e);
		} catch (NoSuchMethodException e) {
			throw new BeanAccessException("在读取bean[" + bean + "]的属性[" + name
					+ "]时遇到错误", e);
		}
	}

	/**
	 * 调用指定的方法
	 * 
	 * @param object
	 * @param methodName
	 * @param args
	 *            方法参数，不能用null值
	 * @return
	 */
	public static Object invokeMethod(Object object, String methodName,
			Object[] args, Class[] parameterTypes) {
		try {
			// 试着将参数转换成合法的参数类型
			if (args != null && args.length > 0 && parameterTypes != null
					&& parameterTypes.length > 0
					&& args.length == parameterTypes.length) {
				Object[] args2 = new Object[args.length];
				for (int i = 0; i < args.length; i++) {
					args2[i] = DataUtils.as(args[i], parameterTypes[i]);
				}
				args = args2;
			}
			return MethodUtils.invokeMethod(object, methodName, args,
					parameterTypes);
		} catch (NoSuchMethodException e) {
			throw new BeanAccessException("调用bean[" + object + "的方法["
					+ methodName + "]时遇到一个错误", e);
		} catch (IllegalAccessException e) {
			throw new BeanAccessException("调用bean[" + object + "的方法["
					+ methodName + "]时遇到一个错误", e);
		} catch (InvocationTargetException e) {
			throw new BeanAccessException("调用bean[" + object + "的方法["
					+ methodName + "]时遇到一个错误", e);
		}
	}

	/**
	 * 返回一个bean的Map形式
	 * 
	 * @param bean
	 * @param ignoreProperties
	 *            要忽略的属性名
	 * @return
	 */
	public static Map<String, Object> describe(Object bean, String[] ignoreProperties) {
		if (bean == null)
			return null;
		if (bean instanceof DynaBean) {
			try {
				return BeanUtilsBean.getInstance().describe(bean);
			} catch (IllegalAccessException e) {
				throw new BeanAccessException("在将Bean[" + bean
						+ "]转换成Map形式时遇到一个错误:", e);
			} catch (InvocationTargetException e) {
				throw new BeanAccessException("在将Bean[" + bean
						+ "]转换成Map形式时遇到一个错误:", e);
			} catch (NoSuchMethodException e) {
				throw new BeanAccessException("在将Bean[" + bean
						+ "]转换成Map形式时遇到一个错误:", e);
			}
		}

		Map<String, Object> description = new HashMap<String, Object>();
		PropertyDescriptor[] descriptors = BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(bean);
		Class clazz = bean.getClass();
		for (int i = 0; i < descriptors.length; i++) {
			String name = descriptors[i].getName();
			boolean ignore = false;
			if (ignoreProperties != null) {
				for (String ignorePro : ignoreProperties) {
					if (name.equals(ignorePro)) {
						ignore = true;
						break;
					}
				}
			}
			if (ignore)
				continue;
			if (MethodUtils.getAccessibleMethod(clazz,
					descriptors[i].getReadMethod()) != null) {
				description.put(name, getSimpleProperty(bean, name));
			}
		}
		return description;

	}

	/**
	 * 返回一个bean的Map形式
	 * 
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> describe(Object bean) {
		return describe(bean, null);
	}

	public static void printBean(PrintStream ps, Object bean,
			String... ignoreProperties) {
		ps.println("start printBean:" + bean.getClass());
		if (bean == null) {
			ps.println("null");
			return;
		}
		Map<String, Object> map = describe(bean, ignoreProperties);
		for (Map.Entry<String, Object> ent : map.entrySet()) {
			ps.println(ent.getKey() + ": " + ent.getValue());
		}
		ps.println("end printBean:" + bean.getClass());
	}

	public static void printBean(Object bean, String... ignoreProperties) {
		printBean(System.out, bean, ignoreProperties);
	}

	/**
	 * 打印两个bean不同的属性
	 * 
	 * @param ps
	 * @param bean1
	 * @param bean2
	 * @param ignoreProperties
	 */
	public static void printCompareBeans(PrintStream ps, Object bean1,
			Object bean2, String... ignoreProperties) {
		ps.println("start compare " + bean1.getClass() + "," + bean2.getClass());
		Map<String, Object> map1 = describe(bean1, ignoreProperties);
		Map<String, Object> map2 = describe(bean2, ignoreProperties);
		Set<String> keySet1 = map1.keySet();
		Set<String> keySet2 = map2.keySet();
		for (String key : keySet1) {
			Object value1 = map1.get(key);
			Object value2 = map2.get(key);
			if (!equals(value1, value2)) {
				ps.println("key=" + key);
				ps.println("value1=" + value1);
				ps.println("value2=" + value2);
			}
			keySet2.remove(key);
		}
		for (String key : keySet2) {
			Object value1 = map1.get(key);
			Object value2 = map2.get(key);
			if (!equals(value1, value2)) {
				ps.println("key=" + key);
				ps.println("value1=" + value1);
				ps.println("value2=" + value2);
			}
		}
		ps.println("end compare " + bean1.getClass() + "," + bean2.getClass());
	}

	/**
	 * 打印两个bean不同的属性
	 * 
	 * @param bean1
	 * @param bean2
	 * @param ignoreProperties
	 */
	public static void printCompareBeans(Object bean1, Object bean2,
			String... ignoreProperties) {
		printCompareBeans(System.out, bean1, bean2, ignoreProperties);
	}

	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == null) {
			return obj2 == null;
		}
		return obj1.equals(obj2);
	}

	/**
	 * 返回类对于指定超类或接口实现的类型参数数组
	 * 
	 * <pre>
	 * 例如有如下定义：
	 * class A<T>{} 
	 * 
	 * interface IA<T1,T2>{} 
	 * 
	 * class B extends A<String> implements IA<Double,Integer>{}
	 * 
	 * class C<T1,T2> extends A<T1> implements IA<T2,Integer>{}
	 * 
	 * class C2 extends C<Double,String>{}
	 * 
	 * class D extends C{}
	 * 
	 * getTypeArguments(B.class,A.class) 返回[String.class]
	 * getTypeArguments(B.class,IA.class) 返回[Double.class,Integer.class]
	 * getTypeArguments(C2.class,A.class) 返回[Double.class]
	 * getTypeArguments(C2.class,IA.class) 返回[String.class,Integer.class]
	 * getTypeArguments(C.class,IA.class) 返回[null,Integer.class]
	 * getTypeArguments(D.class,C.class) 返回null
	 * </pre>
	 * 
	 * @param cls
	 * @param superType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Type[] getTypeArguments(Class cls, Class superType) {
		if (superType.isAssignableFrom(cls))
			return searchTypeArguments(cls, cls, superType);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	private static Type[] searchTypeArguments(Class rootCls, Class cls,
			Class superType) {
		Type[] sts = getAllGenericSuperTypes(cls);
		for (Type st : sts) {
			if (superType == st) {

			} else if (st instanceof ParameterizedType && ((ParameterizedType) st).getRawType() == superType) {
				Type[] rs = ((ParameterizedType) st).getActualTypeArguments();
				for (int i = 0; i < rs.length; i++) {
					if (rs[i] instanceof TypeVariable) {
						rs[i] = getTypeArgumentsForTypeVariable(rootCls, cls, (TypeVariable) rs[i]);
					}
				}
				return rs;
			}
		}
		for (Type st : sts) {
			Type[] rs = null;
			if (st instanceof Class) {
				rs = searchTypeArguments(rootCls, (Class) st, superType);
			} else if (st instanceof ParameterizedType) {
				rs = searchTypeArguments(rootCls,
						(Class) (((ParameterizedType) st).getRawType()),
						superType);
			}
			if (rs != null)
				return rs;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static Type getTypeArgumentsForTypeVariable(Class cls,
			Class superType, TypeVariable typeVariable) {
		int k = -1;
		for (int i = 0; i < superType.getTypeParameters().length; i++) {
			if (typeVariable == superType.getTypeParameters()[i]) {
				k = i;
				break;
			}
		}
		if (k >= superType.getTypeParameters().length || k < 0) {
			throw new RuntimeException(typeVariable + "不是" + superType
					+ "的类型参数变量");
		}
		return searchTypeArgumentsForTypeVariable(cls, cls, superType, k);
	}

	@SuppressWarnings("unchecked")
	private static Type searchTypeArgumentsForTypeVariable(Class rootCls,
			Class cls, Class superType, int i) {
		Type[] sts = getAllGenericSuperTypes(cls);
		for (Type st : sts) {
			if (superType == st) {

			} else if (st instanceof ParameterizedType
					&& ((ParameterizedType) st).getRawType() == superType) {
				Type t = ((ParameterizedType) st).getActualTypeArguments()[i];
				if (t instanceof TypeVariable) {
					Type t2 = getTypeArgumentsForTypeVariable(rootCls, cls,
							(TypeVariable) t);
					if (t2 != null)
						t = t2;
				}
				return t;
			}
		}
		for (Type st : sts) {
			Type r = null;
			if (st instanceof Class) {
				r = searchTypeArgumentsForTypeVariable(rootCls, (Class) st,
						superType, i);
			} else if (st instanceof ParameterizedType) {
				r = searchTypeArgumentsForTypeVariable(rootCls,
						(Class) (((ParameterizedType) st).getRawType()),
						superType, i);
			}
			if (r != null)
				return r;
		}
		return null;
	}

	private static Type[] getAllGenericSuperTypes(Class cls) {
		Type[] superTypes;
		Type[] ts = cls.getGenericInterfaces();
		if (ts != null && ts.length > 0) {
			superTypes = new Type[ts.length + 1];
			System.arraycopy(ts, 0, superTypes, 1, ts.length);
		} else {
			superTypes = new Type[1];
		}
		superTypes[0] = cls.getGenericSuperclass();
		return superTypes;
	}

    /**
     * 返回bean是否有指定的属性
     *
     * @param bean
     * @param name
     * @return
     */
    public static boolean hasProperty(Object bean, String name) {
        if (bean == null || StringUtils.isBlank(name))
            return false;
        return getPropertyDescriptor(bean.getClass(), name) != null;
    }

    public static PropertyDescriptor getPropertyDescriptor(Class beanClass, String proName) {
        PropertyDescriptor result = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if(propertyDescriptor.getName().equals(proName)) {
                    result = propertyDescriptor;
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return result;
//        return org.springframework.beans.BeanUtils.getPropertyDescriptor(
//                beanClass, proName);
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class beanClass) {
        PropertyDescriptor[] propertyDescriptors = new PropertyDescriptor[0];
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            propertyDescriptors = beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return propertyDescriptors;
//        return org.springframework.beans.BeanUtils.getPropertyDescriptors(beanClass);
    }

	public static boolean isSimpleValueType(Class clazz) {
        return clazz.isPrimitive() || clazz.isEnum() ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                clazz.equals(URI.class) || clazz.equals(URL.class) ||
                clazz.equals(Locale.class) || clazz.equals(Class.class);
//		return org.springframework.beans.BeanUtils.isSimpleValueType(clazz);
	}

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return primitiveWrapperTypeMap.containsKey(clazz);
    }

	// 测试getTypeArguments
	// class A<T> {
	// }
	//
	// interface IA<T1, T2> {
	// }
	//
	// class B extends A<String> implements IA<Double, Integer> {
	// }
	//
	// class C<T1, T2> extends A<T1> implements IA<T2, Integer> {
	// }
	//
	// class C2 extends C<Double, String> {
	// }
	//
	// class D extends C {
	//
	// }
	//
	// private static void test(Class cls, Class superCls) {
	// System.out.println("test:" + cls + "," + superCls);
	// Type[] ts = getTypeArguments(cls, superCls);
	// if (ts != null) {
	// String str = "[";
	// for (int i = 0; i < ts.length; i++) {
	// if (i > 0)
	// str += ",";
	// str += ts[i];
	// }
	// str += "]";
	// System.out.println(str);
	// }
	// else
	// System.out.println(ts);
	//
	// }
	//
	// public static void main(String[] args) {
	// test(B.class, A.class);
	// test(B.class, IA.class);
	// test(C2.class, A.class);
	// test(C2.class, IA.class);
	// test(C.class, IA.class);
	// test(IA.class, IA.class);
	// test(D.class, C.class);
	// }
	/**
	 * 将一个JavaBean风格对象的属性值拷贝到另一个对象的同名属性中
	 * （如果不存在同名属性的就不拷贝）
	 * 本方法目前只为系统Bo转换为报文对象内部对象时的属性复制服务，其它功能请慎用。
	 */
	@SuppressWarnings("unchecked")
	public static void copyProperties(Object target, Object source)
			throws Exception {
		/*
		 * 分别获得源对象和目标对象的Class类型对象,Class对象是整个反射机制的源头和灵魂！
		 * 
		 * Class对象是在类加载的时候产生,保存着类的相关属性，构造器，方法等信息
		 */
		Class sourceClz = source.getClass();
		Class targetClz = target.getClass();
		// 得到Class对象所表征的类的所有属性(包括私有属性)
		Field[] fields = sourceClz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			String targetFieldName = fieldName.toLowerCase();
			Field targetField = null;
			try {
				// 得到targetClz对象所表征的类的名为fieldName的属性，不存在就进入下次循环
				targetField = targetClz.getDeclaredField(targetFieldName);
			} catch (SecurityException e) {
				e.printStackTrace();
				break;
			} catch (NoSuchFieldException e) {
				continue;
			}
			// 判断sourceClz字段类型和targetClz同名字段类型是否相同
			if (fields[i].getType() == targetField.getType() 
					|| (fields[i].getType() == Long.class && targetField.getType() == String.class)
					|| (fields[i].getType() == Integer.class && targetField.getType() == String.class)
					|| (fields[i].getType() == Double.class && targetField.getType() == String.class)
					|| (fields[i].getType() == Float.class && targetField.getType() == String.class)
					|| (fields[i].getType() == Date.class && targetField.getType() == String.class)){
				// 由属性名字得到对应get和set方法的名字
				String getMethodName = "get"
						+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				String setMethodName = "set"
						+ targetFieldName.substring(0, 1).toUpperCase()
						+ targetFieldName.substring(1);
				// 由方法的名字得到get和set方法的Method对象
				Method getMethod;
				try {
					getMethod = sourceClz.getDeclaredMethod(getMethodName,
							new Class[] {});
					Method setMethod = targetClz.getDeclaredMethod(
							setMethodName, String.class);
					// 调用source对象的getMethod方法
					Object result = getMethod.invoke(source, new Object[] {});
					if(fields[i].getType() == Date.class){
						if(result != null){
							Date date = (Date) result;
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							result = dateFormat.format(date);
						}
					}
					if(result == null){
						result = "";
					}
					// 调用target对象的setMethod方法
					setMethod.invoke(target, result==null?null:new String(result.toString()));
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				new Exception("同名属性类型不匹配！").printStackTrace();
			}
		}
	}
}
