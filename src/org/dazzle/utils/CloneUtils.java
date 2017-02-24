package org.dazzle.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dazzle.common.exception.BaseException;

/** @author hcqt@qq.com */
public class CloneUtils {

	/** @author hcqt@qq.com */
	public static final <T> T clone(T src) {
    	try {
    		return deepClone(src);
    	} catch (Throwable e) {
    		return simpleClone(src);
    	}
    }

    /** 深拷贝对象，需要对象实现java.io.Serializable接口
     * @author hcqt@qq.com */
    @SuppressWarnings("unchecked")
	public static final <T> T deepClone(T src) {
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		ByteArrayInputStream bytein = null;
		ObjectInputStream in = null;
		try {
			try {
				out = new ObjectOutputStream(byteout);
			} catch (IOException e) {
				throw new BaseException("cloneUtils_83m2j", "克隆对象时创建输出流失败，详情——{0}", e, EU.out(e));
			}
			try {
				out.writeObject(src);
			} catch (IOException e) {
				throw new BaseException("cloneUtils_93jkA", "克隆对象时无法将对象写入输出流，详情——{0}", e, EU.out(e));
			}
			bytein = new ByteArrayInputStream(byteout.toByteArray());
			try {
				in = new ObjectInputStream(bytein);
			} catch (IOException e) {
				throw new BaseException("cloneUtils_832kj", "克隆对象时无法从输出流读取对象，详情——{0}", e, EU.out(e));
			}
			T dest;
			try {
				dest = (T) in.readObject();
			} catch (ClassNotFoundException e) {
				throw new BaseException("cloneUtils_k3k2F", "克隆对象时无法从对象输入流找到对象对应的Class类型，详情——{0}", e, EU.out(e));
			} catch (IOException e) {
				throw new BaseException("cloneUtils_32UGF", "克隆对象时无法从对象输入流读取对象，详情——{0}", e, EU.out(e));
			}
			return dest;
		} finally {
			if(out != null) {
				try { out.close(); } catch (Throwable e) { }
			}
			if(bytein != null) {
				try { bytein.close(); } catch (Throwable e) { }
			}
			if(in != null) {
				try { in.close(); } catch (Throwable e) { }
			}
			if(byteout != null) {
				try { byteout.close(); } catch (Throwable e) { }
			}
		}
    }

    /** 简单克隆对象，即利用JVM自带的克隆机制进行克隆，注意：需要被克隆的对象实现java.lang.Cloneable接口
     * @author hcqt@qq.com */
	@SuppressWarnings("unchecked")
	public static final <T> T simpleClone(T obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Cloneable) {
			Class<?> clazz = obj.getClass();
			Method m;
			try {
				m = clazz.getMethod("clone", (Class[]) null);
			} catch (NoSuchMethodException e) {
				throw new BaseException("cloneUtils_i23kL", "对象内不存在clone()方法，无法克隆", e);
			}
			try {
				return (T) m.invoke(obj);
			} catch (IllegalAccessException e) {
				throw new BaseException("cloneUtils_823jD", "无法克隆对象，没有clone()方法的访问权限", e);
			} catch (IllegalArgumentException e) {
				throw new BaseException("cloneUtils_83jMl", "无法克隆对象，clone()方法形参出错", e);
			} catch (InvocationTargetException e) {
				throw new BaseException("cloneUtils_jk3hg", "无法克隆对象，对象中存在未捕获异常，详情——{0}", e, EU.out(e));
			}
		}
		throw new BaseException("cloneUtils_63yuh", "无法克隆对象，因为对象没有实现java.lang.Cloneable接口");
	}

}
