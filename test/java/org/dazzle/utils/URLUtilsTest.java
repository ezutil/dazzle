package org.dazzle.utils;

import java.net.URI;

import org.junit.Test;

public class URLUtilsTest {

	@Test
	public void test1() {
		URI uri = URI.create("classpath://////a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test2() {
		URI uri = URI.create("classpath:/a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test3() {
		URI uri = URI.create("classpath:a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test4() {
		URI uri = URI.create("classpath://a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test5() {
		URI uri = URI.create("classpath:../../a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test6() {
		URI uri = URI.create("classpath:./../a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test7() {
		URI uri = URI.create("classpath:../a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test8() {
		URI uri = URI.create("classpath:/../a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test9() {
		URI uri = URI.create("http://www.google.com:8080/index.html");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test10() {
		URI uri = URLUtils.create("classpath:../a/b");
		System.out.println(URLUtils.resolve(uri));
	}

	@Test
	public void test11() {
		URI uri = URLUtils.create("classpath:./a/b");
		System.out.println(uri);
	}

	@Test
	public void test12() {
		URI uri = URLUtils.create("classpath:.\\a\\b");
		System.out.println(uri);
	}

	@Test
	public void test13() {
		System.out.println(URLUtils.resolve("classpath:.\\a\\b"));
	}

	@Test
	public void test14() {
		System.out.println(URLUtils.resolve("classpath:.\\a\\中文/目录"));
	}

	@Test
	public void test15() {
		System.out.println(UU.resolve("classpath:.\\a\\中文/目录"));
	}

}
