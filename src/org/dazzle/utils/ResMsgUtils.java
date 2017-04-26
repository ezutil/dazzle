package org.dazzle.utils;

/** @author hcqt@qq.com */
public class ResMsgUtils {

	/**
	 * @param msg 资源消息，例如：“取消Ups运单，不存在shipmentNo为{0}运单”
	 * @param parameters 资源消息当中的参数，用来替换资源文件当中的占位符
	 * @return 按照资源消息，把资源消息当中的参数替换后返回
	 * @author hcqt@qq.com
	 */
	public static final String resolve(String msg, Object... parameters) {
		if(msg == null || msg.isEmpty() || parameters == null || parameters.length <= 0) {
			return msg;
		}
		for (int i = 0; i < parameters.length; i++) {
			msg = SU.replace(
					msg, 
					new StringBuilder().append("{").append(i).append("}").toString(), 
					DTU.cvt(String.class, parameters[i]));
		}
		return msg;
	}

}
