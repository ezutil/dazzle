package org.dazzle.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dazzle.common.exception.BaseException;
import org.dazzle.utils.IOUtils.CStream;

/**本软件为开源项目，最新项目发布于github，可提交您的代码到本开源软件，项目网址：<a href="https://github.com/ezutil/dazzle">https://github.com/ezutil/dazzle</a><br />
 * 本软件内的大多数方法禁止Override，原因是作者提倡组合，而非继承，如果您确实需要用到继承，而又希望用本软件提供的方法名称与参数列表，建议您自行采用适配器设计模式，逐个用同名方法包裹本软件所提供的方法，这样您依然可以使用继承
 * @author hcqt@qq.com*/
public class NetUtils {

	private static final Logger LOGGER = Logger.getLogger(NetUtils.class);

	/** @author hcqt@qq.com */
	NetUtils() { super(); }

	/** @author hcqt@qq.com */
	public static final void printPlainText(
			HttpServletResponse response, 
			String content) throws NetException {
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "text/plain; charset=utf-8");
		print(response, header, content);
	}

	/** @author hcqt@qq.com */
	public static final void printJsonText(
			HttpServletResponse response, 
			String content) throws NetException {
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=utf-8");
		print(response, header, content);
	}

	/** @author hcqt@qq.com */
	public static final void print(
			HttpServletResponse response, 
			Map<String, String> header, 
			String content) throws NetException {
		if(null == header) {
			header = new HashMap<String, String>();
		}
		if(null == MU.getIgnoreCaseTrim(header, "Content-Type")) {
			header.put("Content-Type", "text/html; charset=utf-8");
		}
		for (Entry<String, String> item : header.entrySet()) {
			if(null == item.getKey()) {
				continue;
			}
			response.setHeader(item.getKey(), item.getValue());
		}
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(content);
		} catch (IOException e) {
			throw new NetException("net_utils_UWnk3", "从response获取out失败，详情——{0}", e, EU.out(e));
		} finally {
			out.flush();
		}
	}

	/** @see #httpRead(URL, String, Map, String)
	 * @author hcqt@qq.com */
	public static final String httpRead(
			final URI uri, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final Map<String, Object> urlParam) throws NetException {
		try {
			return httpRead(uri.toURL(), requestMethod, requestProperty, urlParam);
		} catch (MalformedURLException e) {
			throw new NetException(err98Code, err98Msg, e, e.getMessage());
		}
	}

	/** @see #httpRead(URL, String, Map, String)
	 * @author hcqt@qq.com */
	public static final String httpRead(
			final URL url, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final Map<String, Object> urlParam) throws NetException {
		return httpRead(url, requestMethod, requestProperty, urlParamToQuery(urlParam, getEnc(requestProperty)));
	}

	private static final String getEnc(Map<String, String> requestProperty) {
		String enc = null;
		if(requestProperty != null && requestProperty.get("Content-Type") != null) {
			enc = SU.subStringAfter(requestProperty.get("Content-Type"), "=", -1, true);
		}
		return enc;
	}

	/** @see #httpRead(URL, String, Map, String)
	 * @author hcqt@qq.com */
	public static final String httpRead(
			final URI uri, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final String urlQuery) throws NetException {
		return httpRead(UU.resolveToURL(uri), requestMethod, requestProperty, urlQuery);
	}

	/**
	 * 打开URL连接，一次性读出连接的所有内容
	 * @param url
	 * @param requestMethod
	 * @param contentType 如果为空，则连接URL的时候不会指定Content-Type
	 * @param urlParam 调用URL时候的参数，此方法不会自动进行URL编解码，如需编码，请自己编码
	 * @return
	 * @throws NetException 
	 * {
	 *     {@value #err100Code}:{@value #err100Msg},
	 *     {@value #err99Code}:{@value #err99Msg},
	 *     {@value #err96Code}:{@value #err96Msg},
	 *     {@value #err95Code}:{@value #err95Msg},
	 *     {@value #err94Code}:{@value #err94Msg}
	 * }
	 * @author hcqt@qq.com
	 */
	public static final String httpRead(
			final URL url, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final String urlQuery) throws NetException {
		InputStream inputStream = null;
		if(urlQuery != null) {
			inputStream = CStream.createInputStream(urlQuery.getBytes());
		}
		InputStream responseStream = httpRead(url, requestMethod, requestProperty, inputStream, null, null, null, null);
		if(responseStream == null) {
			return null;
		}
		return IOU.readText(responseStream);
	}
	
	/** @author hcqt@qq.com */
	public static final void httpRead(URL url, HttpRead httpRead) throws NetException {
		HttpURLConnection httpConn = null;
		try {
			try {
				httpConn = (HttpURLConnection) url.openConnection();
			} catch (ClassCastException e) {
				throw new NetException("net_utils_4jjhs", "您传入的url的协议可能不是http", e);
			}
			httpRead.before(httpConn);
			httpConn.connect();
			httpRead.after(httpConn);
		} catch (Exception e) {
			catchException(e);
		} finally {
			if(null != httpConn) {
				try { httpConn.disconnect(); } catch (Throwable e) { }
			}
		}
	}
	
	/** @author hcqt@qq.com */
	public static final InputStream httpRead(
			final URI uri, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final InputStream queryInputStream, 
			final SSLSocketFactory sslSocketFactory, 
			final Integer timeout, 
			final Integer responseOneTimeLoadMemoryThreshold,
			final File responseOverflowThresholdTmpFileDirectory) throws NetException {
		return httpRead(UU.resolveToURL(uri), requestMethod, requestProperty, queryInputStream, sslSocketFactory, timeout, responseOneTimeLoadMemoryThreshold, responseOverflowThresholdTmpFileDirectory);
	}

	/** @author hcqt@qq.com */
	public static final InputStream httpRead(
			final URL url, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final InputStream queryInputStream, 
			final SSLSocketFactory sslSocketFactory, 
			final Integer timeout, 
			final Integer responseOneTimeLoadMemoryThreshold,
			final File responseOverflowThresholdTmpFileDirectory) throws NetException {
		final InputStream[] retInputStream = new InputStream[]{ null };
		httpRead(url, new HttpRead() {
			@Override
			public void before(HttpURLConnection conn) {
				try {
					conn.setRequestMethod(requestMethod);
				} catch (ProtocolException e) {
					throw new NetException("https_read_3PeS3", "地址{1}不支持以{0}方式访问", e, url, requestMethod);
				}
				conn.setInstanceFollowRedirects(true);
				if(sslSocketFactory != null && conn instanceof HttpsURLConnection) {
					((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
				}
				conn.setDoInput(true);
				if(timeout != null) {
					conn.setConnectTimeout(timeout);
					conn.setReadTimeout(timeout);
				}
				if(null != requestProperty) {
					for (Entry<String, String> entry : requestProperty.entrySet()) {
						conn.setRequestProperty(entry.getKey(), entry.getValue());
					}
				}
				if(null != queryInputStream) {
					conn.setDoOutput(true);
					OutputStream outputStream = null;
					try {
						outputStream = conn.getOutputStream();
						IOU.write(queryInputStream, outputStream);
					} catch (IOException e) {
						throw new NetException("https_read_2G9vI", "无法向地址{0}中写入参数", e, url, null, null, conn.getHeaderFields(), url);
					} finally {
						if(outputStream != null) { try {outputStream.close(); } catch (Throwable e) { } }
					}
				}
			}
			
			@Override
			public void after(HttpURLConnection conn) {
				int httpCode = -1;
				try {
					httpCode = conn.getResponseCode();
				} catch (IOException e) {
					throw new NetException("https_read_Um23W", "程序无法获取地址{0}的http响应码，详情——{1}", e, url, null, null, conn.getHeaderFields(), url, e.getMessage());
				}
				InputStream errorStream = null;
				try {
					errorStream = conn.getErrorStream();
					if(errorStream != null) {
						String err = IOUtils.readText(errorStream);
						throw new NetException(err100Code, err100Msg, url, httpCode, err, conn.getHeaderFields(), url.toString(), httpCode, conn.getHeaderFields(), err);
					}
				} finally {
					if(errorStream != null) { try { errorStream.close(); } catch(Throwable e) { } }
				}
				try {
					retInputStream[0] = new ResponseInputStream(conn.getInputStream(), conn.getContentLength(), responseOneTimeLoadMemoryThreshold, responseOverflowThresholdTmpFileDirectory);
				} catch (IOException e) {
					throw new NetException("https_read_92Mw8", "无法对地址{0}的响应数据进行流读取，详情——{1}", e, url.toString(), httpCode, null, conn.getHeaderFields(), url, e.getMessage());
				}
			}
		});
//		if(requestProperty != null && requestProperty.get("Accept-Encoding") != null && requestProperty.get("Accept-Encoding").toLowerCase().contains("gzip")) {
//		}
		// 尝试以gzip封装，如果出错，则退回非压缩流，以响应流魔数为准，不用请求头Accept-Encoding判断，bin流不可关闭，否则会导致读取GZIP流时出现流已关闭异常
		BufferedInputStream bin = new BufferedInputStream(retInputStream[0]);
		bin.mark(0);
		try {
			return new GZIPInputStream(bin);
		} catch (IOException e) {
			try { bin.reset(); } catch (IOException e1) { }
			return bin;
		} finally {
			try { retInputStream[0].close(); } catch (IOException e) { }
		}
	}

	/** @author hcqt@qq.com */
	public static interface HttpRead {
		/** @author hcqt@qq.com */
		void before(HttpURLConnection conn);
		/** @author hcqt@qq.com */
		void after(HttpURLConnection conn);
	}

	/**@author hcqt@qq.com*/
	private static class ResponseInputStream extends InputStream {
		private final InputStream encapsulationInputStream;

		/**用于将流复制到本地文件系统或内存，例如可以用于避免http连接关闭以后，无法读取流的问题
		 * @author hcqt@qq.com*/
		public ResponseInputStream(
				InputStream inputStream, 
				int responseContentLength, 
				Integer responseOneTimeLoadMemoryThreshold, 
				File responseOverflowThresholdTmpFileDirectory) {
			super();
			if(responseOneTimeLoadMemoryThreshold == null || (responseContentLength != -1 && responseContentLength < responseOneTimeLoadMemoryThreshold)) {
				encapsulationInputStream = loadMemory(inputStream);
				LOGGER.debug("####响应流输出到内存成功");
			} else {
				InputStream tmp = null;
				for (int i = 0; i < 2; i++) {
					try {
						tmp = loadFile(inputStream, responseOverflowThresholdTmpFileDirectory);
						break;
					} catch (Throwable e) {
						LOGGER.debug("####响应流输出到文件失败，将再重试一次==>");
						continue;
					}
				}
				if(tmp == null) {
					LOGGER.debug("####响应流输出到文件失败，改为向内存输出==>");
					tmp = loadMemory(inputStream);
				}
				LOGGER.debug("####响应流输出到文件成功");
				encapsulationInputStream = tmp;
			}
		}

		/** @author hcqt@qq.com*/
		private InputStream loadMemory(InputStream inputStream) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			IOU.copy(inputStream, outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		}

		/** @author hcqt@qq.com*/
		private InputStream loadFile(InputStream inputStream, File responseOverflowThresholdTmpFileDirectory) {
			OutputStream outputStream = null;
			File file = null;
			if(responseOverflowThresholdTmpFileDirectory == null) {
				try { file = File.createTempFile("resp", ".tmp"); } catch (IOException e) {
					LOGGER.debug(e);
				}
			} else {
				try {
					file = File.createTempFile("resp", ".tmp", responseOverflowThresholdTmpFileDirectory);
				} catch (Throwable e) {
					try { file = File.createTempFile("resp", ".tmp"); } catch (IOException e1) {
						LOGGER.debug(e1);
					}
				}
			}
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("####响应流将要输出到临时文件==>"+file);
			}
			outputStream = CStream.createOutputStream(file);
			IOU.copy(inputStream, outputStream);
			return CStream.createInputStream(file);
		}

		@Override
		public int read() throws IOException {
			return encapsulationInputStream.read();
		}
	}

	/** @author hcqt@qq.com */
	public static final String appendParameter(String url, Map<String, String[]> parameters, String enc) throws NetException {
		URI uri = appendParameter(UU.resolveToURI(url), parameters, enc);
		if(uri == null) {
			return "";
		}
		return uri.toString();
	}
	
	/** @author hcqt@qq.com */
	public static final URL appendParameter(URL url, Map<String, String[]> parameters, String enc) throws NetException {
		return UU.resolveToURL(appendParameter(UU.resolveToURI(url), parameters, enc));
	}
	
	/** @author hcqt@qq.com */
	public static final URI appendParameter(URI url, Map<String, String[]> parameters, String enc) throws NetException {
		if(parameters == null) {
			return url;
		}
		Map<String, String[]> oldParameter = queryToUrlParam(url, enc);
		if(oldParameter == null) {
			oldParameter = new LinkedHashMap<String, String[]>();
		}
		oldParameter.putAll(parameters);
		String query = urlParamToQuery(oldParameter, enc);
		return UU.create(url.getScheme(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), query, url.getFragment()); 
	}

	/** @author hcqt@qq.com */
	public static final Map<String, String[]> queryToUrlParam(String url, String enc) {
		return queryToUrlParam(UU.resolveToURI(url), enc);
	}

	/** @author hcqt@qq.com */
	public static final Map<String, String[]> queryToUrlParam(URI url, String enc) {
		if(url == null) {
			return null;
		}
		String query = url.getQuery();
		String[] params = SU.split(query, "&");
		if(params == null) {
			return null;
		}
		if(enc == null) {
			enc = Charset.defaultCharset().name();
		}
		Map<String, List<String>> urlParam = new LinkedHashMap<String, List<String>>();
		try {
			for (String keyValMap : params) {
				String[] keyValMapArr = SU.split(keyValMap, "=");
				String key = null;
				if(keyValMapArr.length >= 1) {
					key = keyValMapArr[0] == null ? "" : URLDecoder.decode(keyValMapArr[0], enc);
				} else {
					key = "";
				}
				if(key.contains("[]")) {
					key = SU.subStringBefore(key, "[]");
				}
				String val = null;
				if(keyValMapArr.length >= 2) {
					val = keyValMapArr[1] == null ? "" : URLDecoder.decode(keyValMapArr[1], enc);
				} else {
					val = "";
				}
				if(urlParam.get(key) == null) {
					urlParam.put(key, new ArrayList<String>()); 
				}
				urlParam.get(key).add(val);
			}
		} catch (UnsupportedEncodingException e) {
			throw new NetException("net_utils_2mPeX", "url参数转换字符串失败，不支持字符集编码{0}", e, enc);
		}
		Map<String, String[]> ret = new LinkedHashMap<String, String[]>();
		for (Iterator<Entry<String, List<String>>> it = urlParam.entrySet().iterator(); it.hasNext(); ) {
			Entry<String, List<String>> entry = it.next();
			ret.put(entry.getKey(), DTU.cvt(String[].class, entry.getValue()));
			it.remove();
		}
		return ret;
	}

	/** @author hcqt@qq.com */
	public static final Map<String, String[]> queryToUrlParam(URL url, String enc) {
		return queryToUrlParam(UU.resolveToURI(url), enc);
	}

	private static final String urlParamToQuery(Map<String, ?> urlParam, String enc) {
		if(urlParam == null || urlParam.isEmpty()) {
			return null;
		}
		if(enc == null) {
			enc = Charset.defaultCharset().name();
		}
		StringBuilder query = new StringBuilder();
		try {
			for (Entry<String, ?> entry : urlParam.entrySet()) {
				if(entry.getValue() == null) {
					query.append(entry.getKey() == null ? "" : URLEncoder.encode(entry.getKey(), enc));
					query.append("=");
					query.append("&");
				}
				else if(entry.getValue() instanceof String) {
					query.append(entry.getKey() == null ? "" : URLEncoder.encode(entry.getKey(), enc));
					query.append("=");
					query.append(URLEncoder.encode(DTU.cvt(String.class, entry.getValue()), enc));
					query.append("&");
				}
				else if(entry.getValue().getClass().isArray()) {
					String[] values = DTU.cvt(String[].class, entry.getValue());
					if(values == null) {
						query.append(entry.getKey() == null ? "" : URLEncoder.encode(entry.getKey(), enc));
						query.append("=");
						query.append("&");
					}
					else if(values.length == 1) {
						query.append(entry.getKey() == null ? "" : URLEncoder.encode(entry.getKey(), enc));
						query.append("=");
						query.append(values[0] == null ? "" : URLEncoder.encode(values[0], enc));
						query.append("&");
					}
					else {
						for (String value : values) {
							query.append(entry.getKey() == null ? "" : URLEncoder.encode(entry.getKey(), enc)+"[]");
							query.append("=");
							query.append(URLEncoder.encode(value, enc));
							query.append("&");
						}
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new NetException("net_utils_Om2Vj", "url参数转换字符串失败，不支持字符集编码{0}", e, enc);
		}
		SU.deleteSuffix(query, "&", true);
		return query.toString();
	}
	
	private static final void catchException(Throwable e) {
		if(e instanceof NetException) {
			throw (NetException) e;
		}
		if(e instanceof java.net.ConnectException) {
			throw new NetException(err96Code, err96Msg, e);
		} 
		else if(e instanceof java.net.NoRouteToHostException) {
			throw new NetException(err95Code, err95Msg, e, e.getMessage());
		} 
		else if(e instanceof SocketTimeoutException) {
			throw new NetException(err91Code, err91Msg, e, e.getMessage());
		} 
		else {
			throw new NetException(err94Code, err94Msg, e, EU.out(e));
		}
	}

	public static final String err100Code = "net_utils_58124";
	public static final String err100Msg = "无法正常连接URL——{0}，URL状态号——{1}，http头数据——{2}，错误详情——{3}";
	
	public static final String err98Code = "net_utils_o2hj3";
	public static final String err98Msg = "uri书写不符合规范，您的书写——{0}";
	
	public static final String err97Code = "net_utils_o2hj3";
	public static final String err97Msg = "url书写不符合规范，您的书写——{0}";
	
	public static final String err87Code = "net_utils_Ek3p7";
	public static final String err87Msg = "uri书写不符合规范，您的书写——{0}";
	
	public static final String err96Code = "net_utils_ugws1";
	public static final String err96Msg = "网络连接超时";
	
	public static final String err91Code = "net_utils_9l3kW";
	public static final String err91Msg = "连接超时，详情——{0}";

	public static final String err95Code = "net_utils_k23n5";
	public static final String err95Msg = "连接不到主机，详情——{0}";

	public static final String err94Code = "net_utils_y2ksc";
	public static final String err94Msg = "未知异常，详情——{0}";
	
	public static final String err451Code = "net_utils_y7h3m";
	public static final String err451Msg = "URL-->[{0}]语法错误，这不是一个URL";

	public static final String err99Code = "net_utils_ngs5t";
	public static final String err99Msg = "未知IO异常，详情——{0}";

//	/**@author hcqt@qq.com*/
//	private static final class RequestUtil {
//
//		private static final void parseParameters(Map<String, String[]> map, String data, String encoding)
//		{
//			if ((data != null) && (data.length() > 0))
//			{
//				byte[] bytes = null;
//				try {
//					bytes = data.getBytes(encoding);
//				} catch (UnsupportedEncodingException e) {
//					throw new NetException("net_utils_kj3hO", "字符串{0}无法转换为字符集{1}", e, data, encoding);
//				}
//				parseParameters(map, bytes, encoding);
//			}
//		}
//
//		private static final void parseParameters(Map<String, String[]> map, byte[] data, String encoding) {
//			Charset charset = Charset.forName(encoding);
//			if ((data != null) && (data.length > 0)) {
//				int ix = 0;
//				int ox = 0;
//				String key = null;
//				String value = null;
//				while (ix < data.length) {
//					byte c = data[(ix++)];
//					switch ((char)c) {
//					case '&': 
//						value = new String(data, 0, ox, charset);
//						if (key != null) {
//							putMapEntry(map, key, value);
//							key = null;
//						}
//						ox = 0;
//						break;
//					case '=': 
//						if (key == null) {
//							key = new String(data, 0, ox, charset);
//							ox = 0;
//						} else {
//							data[(ox++)] = c;
//						}
//						break;
//					case '+': 
//						data[(ox++)] = 32;
//						break;
//					case '%': 
//						data[(ox++)] = ((byte)((convertHexDigit(data[(ix++)]) << 4) + convertHexDigit(data[(ix++)])));
//						
//						break;
//					default: 
//						data[(ox++)] = c;
//					}
//				}
//				if (key != null) {
//					value = new String(data, 0, ox, charset);
//					putMapEntry(map, key, value);
//				}
//			}
//		}
//
//		private static final void putMapEntry(Map<String, String[]> map, String name, String value)
//		{
//			String[] newValues = null;
//			String[] oldValues = (String[])map.get(name);
//			if (oldValues == null) {
//				newValues = new String[1];
//				newValues[0] = value;
//			} else {
//				newValues = new String[oldValues.length + 1];
//				System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
//				newValues[oldValues.length] = value;
//			}
//			map.put(name, newValues);
//		}
//		private static final byte convertHexDigit(byte b)
//		{
//			if ((b >= 48) && (b <= 57)) return (byte)(b - 48);
//			if ((b >= 97) && (b <= 102)) return (byte)(b - 97 + 10);
//			if ((b >= 65) && (b <= 70)) return (byte)(b - 65 + 10);
//			throw new NetException("net_utils_Ke38E", "byte数据{0}无法转换为十六进制数字", b);
//		}
//	}

	/** @author hcqt@qq.com */
	public static class NetException extends BaseException {

		private static final long serialVersionUID = 5129811509705065329L;

		private final String url;
		private final Map<String, List<String>> headerFields;
		private final Integer httpCode;
		private final String originalErrorMsg;

		/** @author hcqt@qq.com */
		public NetException() {
			super();
			this.url = null;
			this.headerFields = null;
			this.httpCode = null;
			this.originalErrorMsg = null;
		}

		/** @author hcqt@qq.com */
		public NetException(String code, String message, Object... msgArg) {
			super(code, message, msgArg);
			this.url = null;
			this.headerFields = null;
			this.httpCode = null;
			this.originalErrorMsg = null;
		}

		/** @author hcqt@qq.com */
		public NetException(String code, String message, Throwable cause, Object... msgArg) {
			super(code, message, cause, msgArg);
			this.url = null;
			this.headerFields = null;
			this.httpCode = null;
			this.originalErrorMsg = null;
		}
		
		/** @author hcqt@qq.com */
		public NetException(String code, String message, URL url, Integer httpCode, String originalErrorMsg, Map<String, List<String>> headerFields, Object... msgArg) {
			super(code, message, msgArg);
			this.url = url.toString();
			this.headerFields = headerFields;
			this.httpCode = httpCode;
			this.originalErrorMsg = originalErrorMsg;
		}
		
		/** @author hcqt@qq.com */
		public NetException(String code, String message, Throwable cause, URL url, Integer httpCode, String originalErrorMsg, Map<String, List<String>> headerFields, Object... msgArg) {
			super(code, message, cause, msgArg);
			this.url = url.toString();
			this.headerFields = headerFields;
			this.httpCode = httpCode;
			this.originalErrorMsg = originalErrorMsg;
		}

		/** @author hcqt@qq.com */
		public NetException(String code, String message, String url, Integer httpCode, String originalErrorMsg, Map<String, List<String>> headerFields, Object... msgArg) {
			super(code, message, msgArg);
			this.url = url;
			this.headerFields = headerFields;
			this.httpCode = httpCode;
			this.originalErrorMsg = originalErrorMsg;
		}

		/** @author hcqt@qq.com */
		public NetException(String code, String message, Throwable cause, String url, Integer httpCode, String originalErrorMsg, Map<String, List<String>> headerFields, Object... msgArg) {
			super(code, message, cause, msgArg);
			this.url = url;
			this.headerFields = headerFields;
			this.httpCode = httpCode;
			this.originalErrorMsg = originalErrorMsg;
		}

		/** @author hcqt@qq.com */
		public String getUrl() {
			return url;
		}

		/** @author hcqt@qq.com */
		public Map<String, List<String>> getHeaderFields() {
			return headerFields;
		}

		/** @author hcqt@qq.com */
		public Integer getHttpCode() {
			return httpCode;
		}

		/** @author hcqt@qq.com */
		public String getOriginalErrorMsg() {
			return originalErrorMsg;
		}
		
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final String httpsRead(
			final URI uri, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final Map<String, Object> urlParam) throws NetException {
		return httpsRead(UU.resolveToURL(uri), requestMethod, requestProperty, urlParamToQuery(urlParam, getEnc(requestProperty)));
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final String httpsRead(
			final URL url, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final Map<String, Object> urlParam) throws NetException {
		return httpsRead(url, requestMethod, requestProperty, urlParamToQuery(urlParam, getEnc(requestProperty)));
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final String httpsRead(
			final URI uri, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final String urlQuery) throws NetException {
		try {
			return httpsRead(uri.toURL(), requestMethod, requestProperty, urlQuery);
		} catch (MalformedURLException e) {
			throw new NetException(err98Code, err98Msg, e, e.getMessage());
		}
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final String httpsRead(
			final URL url, 
			final String requestMethod, 
			final Map<String, String> requestProperty, 
			final String urlQuery) throws NetException {
		return httpRead(url, requestMethod, requestProperty, urlQuery);
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final Map<String, String[]> getParameterMap(String url, String enc, boolean needSequence) throws NetException {
		return queryToUrlParam(url, enc);
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final URL uriToUrl(URI uri) throws NetException {
		return UU.resolveToURL(uri);
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final URL strToUrl(String uriOrUrl) throws NetException {
		return UU.resolveToURL(uriOrUrl);
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final URI strToUri(String uriOrUrl) throws NetException {
		return UU.resolveToURI(uriOrUrl);
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final String getUriQuery(String uri) throws NetException {
		return SU.subStringAfter(uri, "?");
	}

	/** @author hcqt@qq.com */
	@Deprecated
	public static final String getUriQueryOther(String uri) throws NetException {
		return SU.subStringBefore(uri, "?");
	}

}
