package org.dazzle.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dazzle.common.exception.BaseException;

/**本软件为开源项目，最新项目发布于github，可提交您的代码到本开源软件，项目网址：<a href="https://github.com/ezutil/dazzle">https://github.com/ezutil/dazzle</a><br />
 * 本软件内的大多数方法禁止Override，原因是作者提倡组合，而非继承，如果您确实需要用到继承，而又希望用本软件提供的方法名称与参数列表，建议您自行采用适配器设计模式，逐个用同名方法包裹本软件所提供的方法，这样您依然可以使用继承
 * @see #get(Class, Map, String)
 * @author hcqt@qq.com*/
public class MapUtils {

	/**@see #get(Class, Map, String)
	 * @author hcqt@qq.com */
	public static final <T> T getIgnoreCase(Map<String, T> map, String key) {
		if(null == map) {
			return null;
		}
		if(null == key) {
			return map.get(null);
		}
		for (Entry<String, T> item : map.entrySet()) {
			if(item.getKey().equalsIgnoreCase(key)) {
				return item.getValue();
			}
		}
		return null;
	}

	/**@see #get(Class, Map, String)
	 * @author hcqt@qq.com */
	public static final <T> T getIgnoreCaseTrim(Map<String, T> map, String key) {
		if(null == map) {
			return null;
		}
		if(null == key) {
			return map.get(null);
		}
		for (Entry<String, T> item : map.entrySet()) {
			if(item.getKey().trim().equalsIgnoreCase(key.trim())) {
				return item.getValue();
			}
		}
		return null;
	}

	/**从map当中获取指定key相应的value，解决多层嵌套map取值的需求，避免了书写重复代码，且健壮性差的问题<br />
	 * 注意，如果你的map嵌套只有3层，但是你书写的表达式有10层，那么是取不到值的，将返回null
	 * @param clazz 取值以何种数据类型返回
	 * @param express 格式：“keyx.keyxx.keyxxx”每一层key之间以“.”分隔
	 * @author hcqt@qq.com */
	@SuppressWarnings("unchecked")
	public static final <T> T get(Class<T> clazz, Map<String, ?> map, String express) {
		if(map == null || map.isEmpty()) {
			return null;
		}
		if(clazz == null) {
			return null;
		}
		String[] keys = null;
		if(express == null) {
			return null;
		} else {
			keys = express.split("\\.");
		}
		if(keys == null || keys.length <= 0) {
			return null;
		}
		for (int i = 0; i < keys.length; i++) {
			if(i == keys.length - 1) {
				return DTU.convert(clazz, map.get(keys[i]));
			} else {
				try {
					map = DTU.convert(Map.class, map.get(keys[i]));
				} catch(BaseException e) {
					if(e.getCode().equals("dataTypeUtils_i4nCf")) {
						return DTU.convert(clazz, map.get(keys[i]));
					} else {
						throw e;
					}
				}
			}
		}
		return DTU.convert(clazz, map.get(keys[keys.length]));
	}

	/**@author hcqt@qq.com */
	public static final void removeNullVal(Map<?, ?> map) {
		if(map == null) {
			return;
		}
		Set<Object> waitRemoveKeys = new HashSet<Object>();
		for (Entry<?, ?> entry : map.entrySet()) {
			if(entry.getValue() == null) {
				waitRemoveKeys.add(entry.getKey());
			}
		}
		for (Object waitRemoveKey : waitRemoveKeys) {
			map.remove(waitRemoveKey);
		}
	}

	/**@author hcqt@qq.com */
	@SuppressWarnings("unchecked")
	public static final <K, V> Map<K, V> put(@SuppressWarnings("rawtypes") Class<? extends Map> clazz, Object... oddEvenKeyValPairs) {
		if(!Map.class.isAssignableFrom(clazz)) {
			throw new BaseException("mapUtils_98k3G", "创建Map时您传入的Class类型{0}不符合规范，要求必须是java.util.Map类型", clazz.getName());
		}
		if(oddEvenKeyValPairs == null) {
			return null;
		}
		if(oddEvenKeyValPairs.length % 2 != 0) {
			throw new BaseException("mapUtils_98k3G", "键值对必须成对出现，您传入的数组个数是奇数，不符合规定，异常数据——{0}", Arrays.toString(oddEvenKeyValPairs));
		}
		Map<K, V> n;
		if(clazz.isInterface()) {
			n = (Map<K, V>) new HashMap<Object, Object>();
		} else {
			try {
				n = (Map<K, V>) clazz.newInstance();
			} catch (Exception e) {
				throw new BaseException("mapUtils_p2hE3", "map无法实例化，详情——{0}", e, EU.out(e));
			}
		}
		for (int i = 0; i < oddEvenKeyValPairs.length; i+=2) {
			if(oddEvenKeyValPairs[i] instanceof String) {
				put(n, (K) oddEvenKeyValPairs[i], (V) oddEvenKeyValPairs[i + 1], clazz);
			} else {
				n.put((K) oddEvenKeyValPairs[i], (V) oddEvenKeyValPairs[i + 1]);
			}
		}
		return n;
	}

	/**@see #get(Class, Map, String)
	 * @author hcqt@qq.com */
	public static final <K, V> Object put(Map<K, V> map, K express, V val) {
		return put(map, express, val, null);
	}

	/**@see #get(Class, Map, String)
	 * @author hcqt@qq.com */
	@SuppressWarnings("unchecked")
	public static final <K, V> Object put(Map<K, V> map, K express, V val, @SuppressWarnings("rawtypes") Class<? extends Map> newMapType) {
		if(map == null) {
			return null;
		}
		if(express == null) {
			return map.put(null, val);
		}
		if(!(express instanceof String)) {
			return map.put(express, val);
		}
		String[] keys = ((String) express).split("\\.");
		if(keys == null) {
			return null;
		}
		if(keys.length <= 0) {
			return null;
		}
		if(newMapType == null) {
			newMapType = HashMap.class;
		}
		if(keys.length == 1) {
			return map.put((K) keys[0], val);
		} 
		else if(keys.length == 2) {
			Object mapVal = map.get(keys[0]);
			if(null == mapVal || !Map.class.isAssignableFrom(mapVal.getClass())) {
				Map<K, V> newMap = put0(newMapType); 
				map.put((K) keys[0], (V) newMap);
				return newMap.put((K) keys[1], val);
			} 
			else {
				Map<String, Object> tmp = (Map<String, Object>) mapVal;
				return tmp.put(keys[1], val);
			}
		}
		else {
			V mapVal = null;
			V prevVal = null;
			for (int i = 0; i < keys.length; i++) {
				// 判断是否到了最后一级节点
				if(i == keys.length -1) {
					return map.put((K) keys[i], val);
				}
				mapVal = map.get(keys[i]);
				if(null == map.get(keys[i])) {
					// 目的是要获取下一级节点的类型，然后递归往下找到最后一级，然后按照指定的key把val进行put
					// 如果下一级节点是空的，那么说明下一级节点根本没有定义过，那么用户肯定是要创建不存在的节点，逐级向下创建，到最后一级节点的时候不再创建map，而是put val
					// 如果下一级节点 不是空的，但是类型不是map，那就无法继续向下递归，所以直接把下一级节点的类型强制改成map，然后逐级向下创建
					// 如果下一级节点就是map，那么get其数据，把get出来的map作为基础继续向下递归，直到最后一级的时候，进行put val
					mapVal = (V) put0(newMapType);
					prevVal = map.put((K) keys[i], mapVal);
					map = (Map<K, V>) mapVal;
				}
				else if(!Map.class.isAssignableFrom(mapVal.getClass())) {
					mapVal = (V) put0(newMapType);
					prevVal = map.put((K) keys[i], mapVal);
					map = (Map<K, V>) mapVal;
				}
				else {
					map = (Map<K, V>) mapVal;
				}
			}
			return prevVal;
		}
	}

	/** @author hcqt@qq.com */
	private static final <T extends Map<? extends String, ?>> T put0(Class<T> newMapType) {
		try {
			return newMapType.newInstance();
		} catch (Exception e) {
			throw new BaseException("ser_map_k43hD", "您传入的类型{0}无法实例化，详情:{1}", e, newMapType.getName(), EU.out(e));
		}
	}

}