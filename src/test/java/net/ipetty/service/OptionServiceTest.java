package net.ipetty.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.test.BaseTest;
import net.ipetty.foundation.service.OptionService;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;

import org.junit.Test;

/**
 * OptionServiceTest
 * 
 * @author luocanfeng
 * @date 2014年6月13日
 */
public class OptionServiceTest extends BaseTest {

	@Resource
	private OptionService optionService;

	@Test
	public void testListByGroup() {
		List<Option> options = optionService.listByGroup(OptionGroup.HUMAN_GENDER);
		for (Option option : options) {
			logger.debug("--{}", option);
		}
	}

}
