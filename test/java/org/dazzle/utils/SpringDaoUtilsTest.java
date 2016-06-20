package org.dazzle.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-servlet.xml","classpath:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class SpringDaoUtilsTest {

	@Autowired
	private JdbcOperations jdbcOperations;

	private static final String sql = "SELECT * FROM user LEFT JOIN log AS la" 
			+ " ON user.id = la.uid" 
			+ " LEFT JOIN log AS lb"
			+ " ON lb.pid = la.id";

	@Test
	public void test() {
		List<Map<String, Object>> list = jdbcOperations.queryForList(sql);
		System.out.println(JU.toJson(list));
	}

	@Test
	public void findTest() {
		Map<String, Object> result = SDU.find(jdbcOperations, "*", "user LEFT JOIN log AS la ON user.id = la.uid LEFT JOIN log AS lb ON lb.pid = la.id", null, null, null, null, null, true);
		System.out.println(result.get("result"));
		System.out.println(result.get("countNum"));
	}
	
	@Test
	public void find2Test() {
		Map<String, Object> result = SDU.find(jdbcOperations, "*", "user LEFT JOIN log AS la ON user.id = la.uid LEFT JOIN log AS lb ON lb.pid = la.id", null, null, null, null, null, true, true);
		System.out.println(result.get("result"));
		System.out.println(result.get("countNum"));
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = DTU.cvt(List.class, result.get("result"));
		List<Map<String, Object>> logResultMapping = SDU.genFieldMapping(
				SDU.genFieldMapping("LOG.ID", "id", null, Long.class, true),
				SDU.genFieldMapping("LOG.PID", "pid", null, Long.class, false),
				SDU.genFieldMapping("LOG.UID", "uid", "id", Long.class, false),
				SDU.genFieldMapping("LOG.TIME", "time", null, Date.class, false),
				SDU.genFieldMapping("LOG.MSGSNAPSHOT", "msgSnapshot", null, String.class, false));
		List<Map<String, Object>> userResultMapping = SDU.genFieldMapping(
				SDU.genFieldMapping("USER.ID", "id", null, Long.class, true),
				SDU.genFieldMapping("USER.USERNAME", "userName", null, String.class, false),
				SDU.genFieldMapping("USER.PASSWORD", "password", null, String.class, false),
				SDU.genFieldMapping(logResultMapping, "log", null, Set.class, false));
		@SuppressWarnings("unchecked")
		Set<Map<String, Object>> formatResult = SDU.formatResultMap(userResultMapping, list, Set.class, null);
		System.out.println(JU.toJson(formatResult));
	}
	
}
