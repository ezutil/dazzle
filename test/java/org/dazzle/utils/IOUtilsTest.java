package org.dazzle.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.junit.Test;

/** @author hcqt@qq.com */
public class IOUtilsTest {
	
	@Test
	public void test11() throws MalformedURLException {
		String txt;
		double tmp = Math.random();
		IOU.write("abc", URI.create("classpath:/a/b/c/a"+tmp+".txt"), true);
		IOU.write("123", URI.create("classpath:/a/b/c/a"+tmp+".txt"), true);
		IOU.copy(UU.resolve("classpath:/a/b/c/a"+tmp+".txt"), new File(UU.resolve("classpath:/a/b/c/b"+tmp+".txt")), true);
		txt = IOU.readText("classpath:/a/b/c/b"+tmp+".txt");
		System.out.println(txt);
	}
	
	@Test
	public void test10() throws MalformedURLException {
		String txt;
		double tmp = Math.random();
		try {
			txt = IOU.readText(new File(UU.resolve("classpath:/a/b/c/b"+tmp+".txt")));
			System.out.println(txt);
		} catch (org.dazzle.utils.IOUtils.IOException e) {
			System.out.println(e.getCode()+"\t\t"+e.getMessage()+"\t\t"+e.getOriginalMsg()+"\t\t"+e.getMsgArgs());
		}
		IOU.write("abc", URI.create("classpath:/a/b/c/a"+tmp+".txt"), true);
		IOU.write("123", URI.create("classpath:/a/b/c/a"+tmp+".txt"), true);
		IOU.copy(new File(UU.resolve("classpath:/a/b/c/a"+tmp+".txt")), "classpath:/a/b/c/b"+tmp+".txt");
		txt = IOU.readText(new File(UU.resolve("classpath:/a/b/c/b"+tmp+".txt")));
		System.out.println(txt);
	}

	@Test
	public void test9() throws MalformedURLException {
		String txt;
		double tmp = Math.random();
		try {
			txt = IOU.readText(new File(URI.create("file:/D:/a/b/c/b"+tmp+".txt")));
			System.out.println(txt);
		} catch (org.dazzle.utils.IOUtils.IOException e) {
			System.out.println(e.getCode()+"\t\t"+e.getMessage()+"\t\t"+e.getOriginalMsg()+"\t\t"+e.getMsgArgs());
		}
		IOU.write("abc", URI.create("file:/D:/a/b/c/a"+tmp+".txt"), true);
		IOU.write("123", new URL("file:/D:/a/b/c/a"+tmp+".txt"), true);
		IOU.copy(new File("D:/a/b/c/a"+tmp+".txt"), "file:/D:/a/b/c/b"+tmp+".txt");
		txt = IOU.readText(new File(URI.create("file:/D:/a/b/c/b"+tmp+".txt")));
		System.out.println(txt);
	}
	
	@Test
	public void test8() {
		IOU.write("abc", "file:/D:/a/b/c/a.txt", true);
		IOU.copy("file:/D:/a/b/c/a.txt", "file:/D:/a/b/c/b.txt");
	}
	
	@Test
	public void test7() {
		IOU.write("abc", "file:/D:/a/b/c/a.txt", true);
		IOU.copy("file:/D:/a/b/c/a.txt", "file:/D:/a/b/c/b.txt");
	}
	
	@Test
	public void test6() {
		IOU.write("abc", "file:/D:/a/b/c/a.txt", true);
		IOU.copy("file:/D:/a/b/c/a.txt", "file:/D:/a/b/c/b.txt");
	}
	
	@Test
	public void test5() {
		IOU.write("abc", "file:/D:/a/b/c/a.txt", true);
		IOU.write("123", "file:/D:/a/b/c/a.txt", true);
		System.out.println(UU.resolve("file:/D:/a/b/c/a.txt"));
		System.out.println(IOU.readText("file:/D:/a/b/c/a.txt"));
	}
	
	@Test
	public void test4() {
		IOU.write("abc", "file:/a.txt", true);
		IOU.write("123", "file:/a.txt", true);
		System.out.println(UU.resolve("file:/a.txt"));
		System.out.println(IOU.readText("file:/a.txt"));
	}
	
	@Test
	public void test3() {
		IOU.write("abc", "classpath:a.txt", true);
		IOU.write("123", "classpath:a.txt", true);
		System.out.println(UU.resolve("classpath:a.txt"));
		System.out.println(IOU.readText("classpath:a.txt"));
	}
	
	@Test
	public void test2() {
		IOU.write("abc", new File("D:/a.txt"), true);
		IOU.write("123", new File("D:/a.txt"), true);
		System.out.println(IOU.readText(new File("D:/a.txt")));
	}

	@Test
	public void test1() {
		IOU.write("abc", new File("D:/a.txt"));
	}

}
