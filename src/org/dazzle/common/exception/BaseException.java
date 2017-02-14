package org.dazzle.common.exception;

/** @author hcqt@qq.com */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 7369919371075409501L;
	
	private final String code;

	private final String originalMsg;

	private final Object[] msgArgs;

	/** 获取异常编码 
	 * @author hcqt@qq.com*/
	public String getCode() {
		return code;
	}

	/** 获取异常提示语的原始参数列表 
	 * @author hcqt@qq.com*/
	public Object[] getMsgArgs() {
		return msgArgs;
	}

	/** 获取异常原始提示语——未经过国际化I18N转译的提示语 
	 * @author hcqt@qq.com*/
	public String getOriginalMsg() {
		return originalMsg;
	}

	/** @author hcqt@qq.com */
	public BaseException() {
		super();
		this.code = null;
		this.originalMsg = null;
		this.msgArgs = null;
	}

	/** @author hcqt@qq.com */
	public BaseException(
			final String code, 
			final String message, 
			final Object... msgArg) {
		super(MsgI18n.getMsg(code, message, msgArg));
		this.code = code;
		this.originalMsg = message;
		this.msgArgs = msgArg;
	}

	/** @author hcqt@qq.com */
	public BaseException(
			final String code, 
			final String message, 
			final Throwable cause, 
			final Object... msgArg) {
		super(MsgI18n.getMsg(code, message, msgArg), cause);
		this.code = code;
		this.originalMsg = message;
		this.msgArgs = msgArg;
	}

}
