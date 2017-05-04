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
		return put(map, express, val, map != null ? map.getClass() : null);
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
		if(newMapType == null) {
			newMapType = HashMap.class;
		}
		String currentLevelKey = SU.subStringBefore(DTU.cvt(String.class, express) , ".");
		// 如果表达式不存在点号前边的值，说明已经到达最后一级，则把val追加到map中，如果不能追加就覆盖
		if(currentLevelKey == null) {
			V nextLevelVal = map.get(express);
			if(!(val instanceof Map) || !(nextLevelVal instanceof Map)) {
				// 覆盖
				return map.put(express, val);
			} else {
				// 追加
				((Map<K, V>) nextLevelVal).putAll((Map<K, V>) val);
				return null;
			}
		}
		// 如果还存在下一级，先检查当前这一级是否存在，如果存在，方可进入下一级put，如果这一级不存在，就创建这一级，然后进入下一级put，到最后一级时候return
		String nextLevelKeys = SU.subStringAfter(DTU.cvt(String.class, express) , ".");
		V currentLevelVal = map.get(currentLevelKey);
		if(
				currentLevelVal == null // 如果下一级不存在就创建一个map放到下一级
				|| !(currentLevelVal instanceof Map) // 如果下一级不是map就覆盖
				) {
			currentLevelVal = (V) put0(newMapType);
			map.put((K) currentLevelKey, currentLevelVal);
		}
		return put((Map<K, V>) currentLevelVal, (K) nextLevelKeys, val, newMapType);
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