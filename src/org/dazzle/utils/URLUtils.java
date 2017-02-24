package org.dazzle.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.dazzle.common.exception.BaseException;

/**本软件为开源项目，最新项目发布于github，可提交您的代码到本开源软件，项目网址：<a href="https://github.com/ezutil/dazzle">https://github.com/ezutil/dazzle</a><br />
 * 本软件内的大多数方法禁止Override，原因是作者提倡组合，而非继承，如果您确实需要用到继承，而又希望用本软件提供的方法名称与参数列表，建议您自行采用适配器设计模式，逐个用同名方法包裹本软件所提供的方法，这样您依然可以使用继承
 * @see #get(Class, Map, String)
 * @author hcqt@qq.com*/
public class URLUtils {

	public static final String SCHEME_CLASSPATH = "classpath";

	/**@author hcqt@qq.com*/
	URLUtils(){ super(); };

	/////////////
	public static final URI resolveToURI(String uri) {
		return resolve(uri);
	}

	/** @author hcqt@qq.com */
	public static final URI resolveToURI(URI uri) {
		return resolve(uri);
	}

	/** @author hcqt@qq.com */
	public static final URI resolveToURI(final URL url) {
		try {
			return resolve(url.toURI());
		} catch (URISyntaxException e) {
			throw new org.dazzle.utils.URLUtils.URLException("URI_UTILS_RESOLVE_9nm3g", "URL“{0}”解析过程中发现语法错误，详情——{1}", e, url, e.getMessage());
		}
	}

	/** @author hcqt@qq.com */
	public static final URL resolveToURL(String uri) {
		try {
			return resolve(uri).toURL();
		} catch (MalformedURLException e) {
			throw new URLException("URI_UTILS_CONVERT_Om2Eh", "URL“{0}”解析过程中发现语法错误，详情——{1}", e, uri, e.getMessage());
		}
	}

	/** @author hcqt@qq.com */
	public static final URL resolveToURL(URL uri) {
		return resolve(uri);
	}

	/** @author hcqt@qq.com */
	public static final URL resolveToURL(URI uri) {
		try {
			return resolve(uri).toURL();
		} catch (MalformedURLException e) {
			throw new URLException("URI_UTILS_CONVERT_83jLm", "URL“{0}”解析过程中发现语法错误，详情——{1}", e, uri, e.getMessage());
		}
	}
	/////////////

	/**@see #resolve(URI)
	 * @author hcqt@qq.com */
	public static final URL resolve(final URL url) {
		try {
			return resolveToURI(url).toURL();
		} catch (MalformedURLException e) {
			throw new URLException("URI_UTILS_CONVERT_8s3kW", "URL“{0}”解析过程中发现语法错误，详情——{1}", e, url, e.getMessage());
		}
	}

	/**如果uri的协议头采用的是classpath，就把uri解析成绝对路径返回<br />
	 * 如果uri的协议头是其他则原样返回
	 * @author hcqt@qq.com */
	public static final URI resolve(final URI uri) {
		if(SCHEME_CLASSPATH.equalsIgnoreCase(uri.getScheme())) {
			URL baseURL = NetUtils.class.getResource("/");
			if(null == baseURL) {
				throw new URLException("URI_UTILS_CLASSPATH_km3Ns", "无法获取程序的classpath路径");
			}
			URI baseURI;
			try {
				baseURI = baseURL.toURI();
			} catch (URISyntaxException e) {
				throw new URLException("URI_UTILS_CLASSPATH_i92nU", "解析URI“{0}”的时候，发现URI语法异常，详情——{1}", e, baseURL, e.getMessage());
			}
			String uriStr = uri.getSchemeSpecificPart();
			for (int i = SU.indexOf(uriStr, "/", 1, true); i == 0; i = SU.indexOf(uriStr, "/", 1, true)) {
				uriStr = SU.deletePrefix(uriStr, "/");
			}
			if(SU.subStringBefore(uriStr, "/") != null) {
				String scheme = SU.subStringBefore(uriStr, "/");
				if(scheme != null) {
					scheme = scheme.trim();
					if(scheme.endsWith(":")) {
						throw new URLException("URI_UTILS_CLASSPATH_73jEm", "错误的classpath格式，不允许包含二级scheme“{0}”", scheme);
					}
				}
			}
			return baseURI.resolve(uriStr);
//			return specialClasspathResolve(baseURI, uriStr);
		}
		return uri;
	}

//	private static final URI specialClasspathResolve(URI baseURI, String uri) {
//		String scheme = SU.subStringBefore(uri, "/");
//		if(scheme == null) {
//			return baseURI.resolve(uri);
//		}
//		scheme = scheme.trim();
//		if(!scheme.endsWith(":")) {// \ / : * ? " < > |
//			return baseURI.resolve(uri);
//		}
//		return create("file:/"+uri);
//	}

	/**@see #create(String)
	 * @see #resolve(URI)
	 * @author hcqt@qq.com*/
	public static final URI resolve(final String uri) {
		return resolve(create(uri));
	}

	/**JDK自带URI.create的升级，自动兼容空值以及转义“\”字符
	 * @see #resolve(URI)
	 * @author hcqt@qq.com */
	public static final URI create(String uri) {
		if(uri == null || uri.trim().isEmpty()) {
			return null;
		}
		uri = uri.replace('\\', '/');
		return URI.create(uri);
	}

	/** @author hcqt@qq.com */
	public static class URLException extends BaseException {

		private static final long serialVersionUID = -8507973954891579825L;

		/** @author hcqt@qq.com */
		public URLException() {
			super();
		}

		/** @author hcqt@qq.com */
		public URLException(String code, String message, Object... msgArg) {
			super(code, message, msgArg);
		}

		/** @author hcqt@qq.com */
		public URLException(String code, String message, Throwable cause, Object... msgArg) {
			super(code, message, cause, msgArg);
		}

	}

}
