package org.dazzle.utils;

import org.junit.Test;

/**
 * @author：email:<a href="jiayaozhong@qq.com">贾耀中</a>
 * @Date 2017年4月25日 下午5:57:21
 */
public class ResMsgUtilsTest {

	@Test
	public void test1() {
		String msg = "{0}+{1}={2}";
		msg = ResMsgUtils.resolve(msg, 2, 3, 5);
		System.out.println(msg);
		msg = "{2}-{0}={1}";
		msg = ResMsgUtils.resolve(msg, 2, 3, 5);
		System.out.println(msg);
	}

}
