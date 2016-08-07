package org.dazzle.utils;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void subStringTest1() {
		String str = "1,2,3,4,5,6,7,8,9";
		System.out.println(StringUtils.subString(str, ",", 2, -7, false));
	}
	
	@Test
	public void subStringTest2() {
		String str = "1xx2xx3xx4xx5xx6xx7xx8xx9";
		System.out.println(StringUtils.subString(str, "xx", 2, -7, false));
	}
	
	@Test
	public void subStringTest3() {
		String str = "1xx2xx3xx4xx5xx6xx7xx8xx9";
		System.out.println(StringUtils.subString(str, "xx", 2, 3, false));
	}
	
	@Test
	public void subStringTest4() {
		String str = "1xx2xx3xx4xx5xx6xx7xx8xx9";
		System.out.println(StringUtils.subString(str, "xx", 2, -6, false));
	}

}
