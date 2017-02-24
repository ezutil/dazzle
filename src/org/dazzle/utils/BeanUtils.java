package org.dazzle.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dazzle.common.exception.BaseException;

/**JavaBean与Map的相互转化，JavaBean与JavaBean的相互转化<br />
 * 兼容父类的字段；底层实现按照Bean内的字段名称进行转换，而非传统的java数据类型继承
 * @author hcqt@qq.com */
public class BeanUtils {

	/** @author hcqt@qq.com */
	BeanUtils() { super(); }

	/**@see #bean2Map(Object, String[], Boolean)
	 * @author hcqt@qq.com */
	public static final <T> Map<String, Object> bean2Map(T bean) {
		return bean2Map(bean, (Object) null, false);
	}

	/**@see #bean2Map(Object, String[], Boolean)
	 * @author hcqt@qq.com */
	public static final <T> Map<String, Object> bean2Map(
			T bean, 
			String[] fields) {
		return bean2Map(bean, fields, false);
	}
	
	/**@see #bean2Map(Object, String[], Boolean)
	 * @author hcqt@qq.com */
	public static final <T> Map<String, Object> bean2Map(
			T bean, 
			String fields) {
		return bean2Map(bean, (Object) fields, false);
	}

	/**@see #bean2Map(Object, String[], Boolean)
	 * @author hcqt@qq.com */
	public static final <T> Map<String, Object> bean2Map(
			T bean, 
			Boolean needSequence) {
		return bean2Map(bean, (String[]) null, needSequence);
	}

	/**@see #bean2Map(Object, String[], Boolean)
	 * @author hcqt@qq.com */
	public static final <T> Map<String, Object> bean2Map(
			T bean, 
			String fields, 
			Boolean needSequence) {
		return bean2Map(bean, (Object) fields, needSequence);
	}

	/**把bean的指定字段放入map
	 * @param fields 指定参与转化你的字段，允许采用逗号分隔的字符串/字符串数组/字符串Collection
	 * @see #bean2Map(Object, String[], Boolean)
	 * @author hcqt@qq.com
	 */
	public static final <T> Map<String, Object> bean2Map(
			T bean, 
			Object fields, 
			Boolean needSequence) {
		if(fields == null) {
			return bean2Map(bean, (String[]) null, needSequence);
		}
		String[] _fields = null;
		if(fields instanceof String) {
			_fields = DTU.cvt(String[].class, ((String) fields).split(","));
		} else {
			try {
				_fields = DTU.cvt(String[].class, fields);
			} catch (BaseException e) {
				throw new BaseException("beanUtils_9k3YP", "转换bean到map，指定字段的书写存在错误，只允许采用逗号分隔的字符串/字符串数组/字符串Collection，您用类型:{0}数据:{1}指定字段是不符合规则的", e, fields.getClass(), fields);
			}
		}
		return bean2Map(bean, _fields, needSequence);
	}

	/**把bean的指定字段放入map
	 * @param bean 数据源bean
	 * @param fields 指定要转换bean的哪些字段
	 * @param needSequence 返回的map是否需要保持指定的顺序
	 * @author hcqt@qq.com */
	@SuppressWarnings("unchecked")
	public static final <T> Map<String, Object> bean2Map(
			T bean, 
			String[] fields, 
			Boolean needSequence) {
		if (null == bean) {
			return null;
		}
		Class<?> clazz = bean.getClass();
		Set<String> fieldSet = null;
		if(null == fields) {
			fieldSet = getAllFieldName(clazz);
		} else {
			fieldSet = DTU.cvt(Set.class, fields);
		}
		if(null == fieldSet) {
			return null;
		}
		Map<String, Object> ret = null;
		if(needSequence == null || !needSequence) {
			ret = new HashMap<String, Object>();
		} else {
			ret = new LinkedHashMap<String, Object>();
		}
		for (String field : fieldSet) {
			if(null == field) {
				continue;
			}
			ret.put(field, getFieldValue(bean, field));
		}
		return ret;
	}

	/**把一个bean的字段逐个放入另一个bean的同名字段内
	 * @see #bean2Bean(Map, Object, Object)
	 * @author hcqt@qq.com */
	public static final <S, T> T bean2Bean(S obj, Class<T> clazz) {
		Set<String> fields = getAllFieldName(clazz);
		if(fields == null) {
			return null;
		}
		Map<String, String> fieldMapping = new HashMap<String, String>(); 
		for (String field : fields) {
			fieldMapping.put(field, field);
		}
		return bean2Bean(fieldMapping, obj, clazz);
	}

	/**@see #bean2Bean(Map, Object, Object)
	 * @author hcqt@qq.com */
	public static final <S, T> T bean2Bean(S obj, T target) {
		if(target == null) {
			return null;
		}
		Set<String> fields = getAllFieldName(target.getClass());
		if(fields == null) {
			return target;
		}
		Map<String, String> fieldMapping = new HashMap<String, String>(); 
		for (String field : fields) {
			fieldMapping.put(field, field);
		}
		return bean2Bean(fieldMapping, obj, target);
	}

	/**@param <S> 源数据的Javabean类型
	 * @param <T> 目标数据类型
	 * @param fieldMapping 源数据bean字段与目标bean字段的映射关系
	 * @param obj 源数据bean
	 * @param clazz 目标数据类型
	 * @see #bean2Bean(Map, Object, Object)
	 * @author hcqt@qq.com */
	public static final <S, T> T bean2Bean(Map<String, String> fieldMapping, S obj, Class<T> clazz) {
		if(null == fieldMapping) {
			return null;
		}
		T ret = null;
		try {
			ret = clazz.newInstance();
		} catch (Exception e) {
			throw new BaseException("beanUtils_8n3lk", "目标对象[{0}]实例化失败，详情——{1}", e, EU.out(e));
		}
		return bean2Bean(fieldMapping, obj, ret);
	}

	/**@param <S> 源数据的Javabean类型
	 * @param <T> 目标数据类型
	 * @param fieldMapping 源数据bean字段与目标bean字段的映射关系
	 * @param obj 源数据bean
	 * @param target 目标数据容器
	 * @author hcqt@qq.com */
	public static final <S, T> T bean2Bean(Map<String, String> fieldMapping, S obj, T target) {
		if(null == fieldMapping) {
			return target;
		}
		if(target == null) {
			return null;
		}
		for (Entry<String, String> entry : fieldMapping.entrySet()) {
			Object fieldNewValue = getFieldValue(obj, entry.getKey());
			setFieldValue(target, entry.getValue(), fieldNewValue);
		}
		return target;
	}

	/** @author hcqt@qq.com */
	public static final <T> T map2Bean(Class<T> beanClazz, Map<String, ?> map){
		if(null == beanClazz || null == map){
			return null;
		}
		T ret = null;
		try {
			ret = beanClazz.newInstance();
		} catch (Exception e) {
			throw new BaseException("beanUtils_9k3hQ", "map无法转换为javaBean，因为JavaBean无法实例化，详情——{0}", e, EU.out(e));
		}
		return map2Bean(ret, map);
	}
	
	/** @author hcqt@qq.com */
	public static final <T> T map2Bean(T beanObj, Map<String, ?> map){
		if(null == beanObj || null == map){
			return null;
		}
		Set<String> fields = getAllFieldName(beanObj.getClass());
		if(fields == null) {
			return null;
		}
		for (String field : fields) {
			setFieldValue(beanObj, field, map.get(field));
		}
		return beanObj;
	}

	/** @author hcqt@qq.com */
	@SuppressWarnings("unchecked")
	public static final <T extends Collection<Map<String, ?>>, S> Collection<S> collectionMap2CollectionBean(Class<S> beanClazz, T collection) {
		if(null == beanClazz || null == collection) {
			return null;
		}
		Collection<S> ret = null;
		try {
			ret = collection.getClass().newInstance();
		} catch (Exception e) {
			throw new BaseException("beanUtils_72jJk", "集合类型实例化失败，详情——{0}", e, e.getMessage());
		}
		for(Map<String, ?> item : collection){
			S obj = map2Bean(beanClazz, item);
			if(null == obj){
				continue;
			}
			ret.add(obj);
		}
		if(!ret.isEmpty()){
			return ret;
		}
		return null;
	}

	/**获取JavaBean的所有字段
	 * @author hcqt@qq.com */
	public static final Set<String> getAllFieldName (Class<?> clazz) {
		if(null == clazz) {
			return null;
		}
		Set<String> ret = new HashSet<String>();
		for (Class<?> currentClass = clazz; currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
			Field[] fields = currentClass.getDeclaredFields();
			for (Field field : fields) {
				ret.add(field.getName());
			}
		}
		return ret;
	}

	/**按照javaBean的get方法取值，若拿不到就直接从字段上取值
	 * @author hcqt@qq.com */
	public static final Object getFieldValue(
			Object obj, 
			String field) {
		if(null == field) {
			return null;
		}
		Method method = null;
		for (Class<?> currentClass = obj.getClass(); currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
			try {
				method = currentClass.getDeclaredMethod(methodName("get", field));
				return method.invoke(obj);
			} catch (Throwable e) {
				try {
					Field _field = currentClass.getDeclaredField(field);
					_field.setAccessible(true);
					return _field.get(obj);
				} catch (Throwable e1) { }
			}
		}
		return null;
	}

	/**按照javaBean的set方法赋值，若拿不到就直接给字段上赋值
	 * @author hcqt@qq.com */
	public static final void setFieldValue(
			Object obj, 
			String field, 
			Object val) {
		if(null == field) {
			return;
		}
		for (Class<?> currentClass = obj.getClass(); currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
			setField(currentClass, obj, field, val);
		}
	}

	/** @author hcqt@qq.com */
	private static final void setField(Class<?> currentClass, Object obj, String fieldName, Object val) {
		Class<?> fieldType = getFieldType(currentClass, fieldName);
		Method method = null;
		try {
			method = currentClass.getDeclaredMethod(methodName("set", fieldName), fieldType);
		} catch (NoSuchMethodException e) {
			reflectSetField(currentClass, obj, fieldName, val);
			return;
		} catch (SecurityException e) {
			reflectSetField(currentClass, obj, fieldName, val);
			return;
		}
		try {
			method.invoke(obj, DTU.cvt(fieldType, val));
		} catch (IllegalAccessException e) {
			reflectSetField(currentClass, obj, fieldName, val);
			return;
		} catch (IllegalArgumentException e) {
			reflectSetField(currentClass, obj, fieldName, val);
			return;
		} catch (InvocationTargetException e) {
			reflectSetField(currentClass, obj, fieldName, val);
			return;
		}
	}

	/** @author hcqt@qq.com */
	private static final void reflectSetField(Class<?> currentClass, Object obj, String fieldName, Object fieldVal) {
		Field _field = getField(currentClass, fieldName);
		if(_field == null) {
			return;
		}
		try {
			_field.setAccessible(true);
		} catch(SecurityException e) {
		}
		try {
			_field.set(obj, DTU.cvt(_field.getType(), fieldVal));
		} catch (Throwable e) {
		}
	}

	/** @author hcqt@qq.com */
	private static final Class<?> getFieldType(Class<?> clazz, String fieldName) {
		Field _field = getField(clazz, fieldName);
		if(_field == null) {
			return Object.class;
		}
		return getFieldType(_field);
	}

	/** @author hcqt@qq.com */
	private static final Field getField(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		}
	}

	/** @author hcqt@qq.com */
	private static final Class<?> getFieldType(Field field) {
		if(field == null) {
			return Object.class;
		}
		return field.getType();
	}

	/** @author hcqt@qq.com */
	private static final String methodName(String prefix, String fieldName) {
		return new StringBuilder().append(prefix).append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).toString();
	}

}