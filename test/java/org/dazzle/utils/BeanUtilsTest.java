package org.dazzle.utils;

import java.util.Map;

import org.junit.Test;

public class BeanUtilsTest {

	@Test
	public void bean2MapTest() {
		TestBean bean = new TestBean();
		bean.setField1(100L);
		bean.setField2("xxx");
		Map<String, Object> ret = BeanUtils.bean2Map(bean, new String[]{"field1","field2"}, true);
		System.out.println(ret);
		bean = BeanUtils.map2Bean(TestBean.class, ret);
		System.out.println(bean);
		ret.put("field1", 99L);
		ret.put("fieldxxxxxxxx", 99L);
		bean = BeanUtils.map2Bean(bean, ret);
		System.out.println(bean);
		bean.setField1(88L);
		TestBean2 bean2 = BeanUtils.bean2Bean(bean, TestBean2.class);
		System.out.println(bean2);
		bean2.setField1(77L);
		bean = BeanUtils.bean2Bean(bean2, bean);
		System.out.println(bean);
	}

}
class TestBean {
	private Long field1;
	private String field2;
	
	public TestBean() {
		super();
	}
	public Long getField1() {
		return field1;
	}
	public void setField1(Long field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestBean [field1=");
		builder.append(field1);
		builder.append(", field2=");
		builder.append(field2);
		builder.append("]");
		return builder.toString();
	}
	
}
class TestBean2 {
	private Long field1;
	private String field2;
	
	public TestBean2() {
		super();
	}
	public Long getField1() {
		return field1;
	}
	public void setField1(Long field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestBean2 [field1=");
		builder.append(field1);
		builder.append(", field2=");
		builder.append(field2);
		builder.append("]");
		return builder.toString();
	}
}