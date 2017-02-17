package org.dazzle.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapUtilsTest {

	@Test
	public void test5() {
		Map<String, Object> otherArgs = new HashMap<String, Object>();
		otherArgs.put("msg", "xxx");
		Map<String, Object> param = MU.put(HashMap.class, 
				"platform", "all", 
				"audience.tag", new String[]{ "a", "b" },
				"message.msg_content", otherArgs, 
				"message.msg_content.code", 10001, 
				"message.msg_content.type", 1
				);
		System.out.println(param);
		System.out.println(JU.toJson(param));
		MU.put(param, "platform", "allx");
		System.out.println(param);
		System.out.println(JU.toJson(param));
		MU.put(param, "platformx", "allx");
		System.out.println(param);
		System.out.println(JU.toJson(param));
		MU.put(param, "platform.x", "allx");
		System.out.println(param);
		System.out.println(JU.toJson(param));
		MU.put(param, "platform.y.z", "allx");
		System.out.println(param);
		System.out.println(JU.toJson(param));
		
	}
	
	@Test
	public void test4() {
		Map<String, Object> otherArgs = new HashMap<String, Object>();
		otherArgs.put("msg", "xxx");
		Map<String, Object> param = MU.put(HashMap.class, 
				"platform", "all", 
				"audience.tag", new String[]{ "a", "b" },
				"message.msg_content", otherArgs, 
				"message.msg_content.code", 10001, 
				"message.msg_content.type", 1
				);
		System.out.println(param);
		System.out.println(JU.toJson(param));
	}

	@Test
	public void test3() {
		Map<String, Object> otherArgs = new HashMap<String, Object>();
		otherArgs.put("msg", "xxx");
		Map<String, Object> param = MU.put(HashMap.class, 
				"msg_content", otherArgs,
				"msg_content.code", 10001, 
				"msg_content.type", 1
				);
		System.out.println(param);
		System.out.println(JU.toJson(param));
	}

	@Test
	public void test2() {
		System.out.println(MapUtils.put(HashMap.class, "xxx.aa","a", 1, 2, new MapUtilsTest(), "ksjdg"));
	}

	@Test
	public void test1() {
		Map<String, Integer> a = new HashMap<String, Integer>();
		System.out.println(MapUtils.put(a, "xxx.aaa", 1, a.getClass()));
		System.out.println(a);
		System.out.println(MapUtils.put(a, "xxx.aaa", 1, a.getClass()));
		System.out.println(a);
	}

}
