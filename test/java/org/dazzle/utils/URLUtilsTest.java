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

}
