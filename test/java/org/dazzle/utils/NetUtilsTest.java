package org.dazzle.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class NetUtilsTest {

	@Test
	public void test10() {
		Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();
		parameters.put("e", new String[]{"f"});
		parameters.put("G", new String[]{"h","i"});
		System.out.println(NU.appendParameter("http://username:password@www.abc.com:8080?a=b&c%5B%5D=d&c%5B%5D=d#aa", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("http://www.abc.com:8080?a=b&c%5B%5D=d&c%5B%5D=d#aa", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("http://www.abc.com:8080?a=b&c%5B%5D=d&c%5B%5D=d", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("http://www.abc.com?a=b&c%5B%5D=d&c%5B%5D=d", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("http://www.abc.com?a=b&c[]=d&c[]=d", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("www.abc.com?a=b&c[]=d&c[]=d", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("abc.com?a=b&c[]=d&c[]=d", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("..?a=b&c[]=d&c[]=d", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("classpath://..?a=b&c[]=d&c[]=d", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("classpath://.?a=b&c[]=d&c[]=d", parameters, "UTF-8"));
		System.out.println(NU.appendParameter("classpath://a/b/c?a=b&c[]=d&c[]=d", parameters, "UTF-8"));
	}

	@Test
	public void test9() throws UnsupportedEncodingException {
		Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();
		parameters.put("e", new String[]{"f"});
		parameters.put("G", new String[]{"h","i"});
		String ret = NU.appendParameter("http://www.abc.com?a=b&c[]=d&c[]=d", parameters, "UTF-8");
		System.out.println(ret);
		System.out.println(URLDecoder.decode(ret, "UTF-8"));
	}
	
	@Test
	public void test8() throws UnsupportedEncodingException {
		Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();
		parameters.put("e", new String[]{"f"});
		parameters.put("G", new String[]{"h","i"});
		String ret = NU.appendParameter("http://www.abc.com?a=b&c%5B%5D=d&c%5B%5D=d", parameters, "UTF-8");
		System.out.println(ret);
		System.out.println(URLDecoder.decode(ret, "UTF-8"));
	}

	@Test
	public void test7() {
		Map<String, String[]> map = NU.queryToUrlParam("http://www.abc.com?a=b&c%5B%5D=d&c%5B%5D=d", "UTF-8");
		System.out.println(JU.toJson(map));
	}
	
	@Test
	public void test6() {
		Map<String, String[]> map = NU.queryToUrlParam("http://www.abc.com?a=b&c%5B%5D=d&c%5B%5D=e", "UTF-8");
		System.out.println(JU.toJson(map));
	}
	
	@Test
	public void test5() {
		Map<String, String[]> map = NU.queryToUrlParam("http://www.abc.com?a=a&b[]=c&b[]=d&b=e", "UTF-8");
		System.out.println(JU.toJson(map));
	}
	
	@Test
	public void test4() {
		Map<String, String[]> map = NU.queryToUrlParam("http://www.abc.com?a=a&b[]=c&b[]=d", "UTF-8");
		System.out.println(JU.toJson(map));
	}
	
	@Test
	public void test3() {
		Map<String, String[]> map = NU.queryToUrlParam("http://www.abc.com?a=a&b=c&b=d", "UTF-8");
		System.out.println(JU.toJson(map));
	}

	@Test
	public void test2() {
		Map<String, String[]> map = NU.queryToUrlParam("http://www.abc.com?a=a&b=c", "UTF-8");
		System.out.println(JU.toJson(map));
	}

	@Test
	public void test1() {
		URL url = UU.resolveToURL("https://www.baidu.com");
//		URL url = UU.resolveToURL("https://sg.search.yahoo.com/search;_ylt=A0LEV2q5DA9Yb3cA0vci4gt.;_ylc=X1MDMjExNDcwODAwMgRfcgMyBGZyAwRncHJpZAMEbl9yc2x0AzAEbl9zdWdnAzAEb3JpZ2luA3NnLnNlYXJjaC55YWhvby5jb20EcG9zAzAEcHFzdHIDBHBxc3RybAMEcXN0cmwDNDUEcXVlcnkDJUU2JUFEJUEzJUU1JTg4JTk5JUU4JUExJUE4JUU4JUJFJUJFJUU1JUJDJThGBHRfc3RtcAMxNDc3MzgxMzE2?fr=sfp&fr2=sb-top-sg.search&iscqry=&p=Can%27t%20canonicalize%20query:%20BadValue:%20unknown%20top%20level%20operator:%20$gte");
//		URL url = UU.resolveToURL("https://en.wikipedia.org/wiki/Can%27t_canonicalize_query:_BadValue:_unknown_top_level_operator:_$gte");
		String requestMethod = "GET";
		Map<String, String> requestProperty = new HashMap<String, String>();
		Map<String, Object> urlQuery = new HashMap<String, Object>();
		String response = NU.httpRead(url, requestMethod, requestProperty, urlQuery);
		System.out.println(response);
	}

}
