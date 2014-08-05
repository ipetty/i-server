package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.system.service.CrushLogService;

import org.junit.Test;

/**
 * CrushLogServiceTest
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrushLogServiceTest extends BaseTest {

	@Resource
	private CrushLogService crushLogService;

	@Test
	public void testSave() {
		crushLogService.save("test crush log");
	}

}
