package net.ipetty.service;

import javax.annotation.Resource;

import net.ipetty.bonuspoint.service.BonusPointService;
import net.ipetty.core.test.BaseTest;

import org.junit.Assert;
import org.junit.Test;

/**
 * BonusPointServiceTest
 * 
 * @author luocanfeng
 * @date 2014年5月27日
 */
public class BonusPointServiceTest extends BaseTest {

	@Resource
	private BonusPointService bonusPointService;

	@Test
	public void testGain() {
		Assert.assertTrue(true);
	}

}
