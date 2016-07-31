package org.dazzle.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapUtilsTest {

	@Test
	public void aa() {
		System.out.println(MapUtils.put(HashMap.class, "xxx.aa","a", 1, 2, new MapUtilsTest(), "ksjdg"));
	}

	@Test
	public void qaa() {
		Map<String, Integer> a = new HashMap<String, Integer>();
		System.out.println(MapUtils.put(a, "xxx.aaa", 1, a.getClass()));
		System.out.println(a);
		System.out.println(MapUtils.put(a, "xxx.aaa", 1, a.getClass()));
		System.out.println(a);
	}

}
