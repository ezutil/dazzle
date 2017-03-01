package org.dazzle.utils;

import java.net.URI;

import org.junit.Test;

public class URLUtilsTest {

	@Test
	public void test19() {
		URI uri = UU.create("http://username:password@www.abc.com:8080?a=b&c%5B%5D=d&c%5B%5D=d#aa");
		System.out.println(uri.getScheme());
		System.out.println(uri.getHost());
		System.out.println(uri.getAuthority());
		System.out.println(uri.getRawFragment());
		System.out.println(uri.getUserInfo());
		System.out.println(uri.getPort());
		System.out.println(uri.getPath());
		System.out.println(uri.getQuery());
		System.out.println(uri.getSchemeSpecificPart());
		System.out.println(uri.getRawFragment());
		System.out.println(uri.getRawAuthority());
		System.out.println(uri.getRawPath());
		System.out.println(uri.getRawSchemeSpecificPart());
		System.out.println(uri.getRawUserInfo());
	}

	@Test
	public void test18() {
		try {
			URI uri = UU.resolve("classpath:/D:/a/b/c/b.txt");
			System.out.println(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			URI uri = UU.resolve("classpath:/Dxx:/a/b/c/b.txt");
			System.out.println(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			URI uri = UU.resolve("classpath:/D/a/b/c/b.txt");
			System.out.println(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		System.out.println(URLUtils.resolve("classpath:a.txt"));
	}
	
	@Test
	public void test16() {
		System.out.println(URLUtils.resolve("classpath:./a.txt"));
	}
	
	@Test
	public void test17() {
		System.out.println(URLUtils.resolve("classpath:../a.txt"));
	}

}
