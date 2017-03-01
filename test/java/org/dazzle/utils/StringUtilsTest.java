package org.dazzle.utils;

import org.junit.Test;

public class StringUtilsTest {
	
	@Test
	public void test12() {
		String str = "a=";
		System.out.println(JU.toJson(SU.split(str, "=")));
	}

	@Test
	public void test11() {
		String str = "1,2xx3,xx5xx6xx7,8xx9";
		String[] strarr = SU.split(str, (String[]) null, false);
		System.out.println(JU.toJson(strarr));
	}
	
	@Test
	public void test10() {
		String str = "1,2xx3,xx5xx6xx7,8xx9";
		String[] strarr = SU.split(str, new String[] {null}, false);
		System.out.println(JU.toJson(strarr));
	}
	
	@Test
	public void test9() {
		String str = "";
		String[] strarr = SU.split(str, new String[] {",", "xx"}, false);
		System.out.println(JU.toJson(strarr));
	}
	
	@Test
	public void test8() {
		String str = null;
		String[] strarr = SU.split(str, new String[] {",", "xx"}, false);
		System.out.println(JU.toJson(strarr));
	}
	
	@Test
	public void test7() {
		String str = "1,2xx3,xx5xx6xx7,8xx9";
		String[] strarr = SU.split(str, new String[] {",", "xx"}, false);
		System.out.println(JU.toJson(strarr));
	}
	
	@Test
	public void test6() {
		String str = "1,2xx3,x4xx5xx6xx7,8xx9";
		String[] strarr = SU.split(str, new String[] {",", "xx"}, false);
		System.out.println(JU.toJson(strarr));
	}

	@Test
	public void test5() {
		String str = "1xx2xx3xxx4xx5xx6xx7xx8xx9";
		String[] strarr = SU.split(str, "xxx", false);
		System.out.println(JU.toJson(strarr));
	}

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
