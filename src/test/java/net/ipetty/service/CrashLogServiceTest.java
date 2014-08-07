package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.system.domain.CrashLog;
import net.ipetty.system.service.CrashLogService;

import org.junit.Test;

/**
 * CrashLogServiceTest
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrashLogServiceTest extends BaseTest {

	@Resource
	private CrashLogService crashLogService;

	@Test
	public void testSave() {
		crashLogService.save(new CrashLog(null, "user", "4.1.2", 8, "1.0.8", "crash", "test crash log"));
	}

}
