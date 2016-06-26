package org.dazzle.utils;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.Test;

public class JsonUtilsTest {

	@Test
	public void aa() {
	    String json = 
		      ""
		    + "{"
		    + "    '1': {"
		    + "        '1.1': {"
		    + "            '1.1.1': ["
		    + "                'aaaa',"
		    + "                'bbbb'"
		    + "            ],"
		    + "            '1.1.2': ["
		    + "                'cccc',"
		    + "                'dddd'"
		    + "            ]"
		    + "        },"
		    + "        '1.2': ["
		    + "            'eeee',"
		    + "            'ffff'"
		    + "        ],"
		    + "        '1.3': 'jjjj'"
		    + "    }"
		    + "}"
		    + "";
		@SuppressWarnings("rawtypes")
		Map map = (Map) JsonUtils.toObj(json);
		@SuppressWarnings("rawtypes")
		Map map2 = (Map) JsonUtils.toObj(json, LinkedList.class, ConcurrentSkipListMap.class);// ConcurrentHashMap
		System.out.println("json字符串转成对象-->"+map);// 如果是转自定义对象的代码，直接使用google的代码即可
		System.out.println("json字符串转成对象-->"+map2);
		System.out.println("对象转成json字符串-->"+JsonUtils.toJson(map));
	}

}
