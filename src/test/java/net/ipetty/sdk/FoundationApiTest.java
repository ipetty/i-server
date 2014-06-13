package net.ipetty.sdk;

import java.util.List;

import net.ipetty.core.test.BaseTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;

import org.junit.Assert;
import org.junit.Test;

/**
 * FoundationApiTest
 * 
 * @author luocanfeng
 * @date 2014年6月13日
 */
public class FoundationApiTest extends BaseTest {

	FoundationApi foundationApi = new FoundationApiImpl(ApiContext.getInstance());

	@Test
	public void testListOptionsByGroup() {
		List<Option> options = foundationApi.listOptionsByGroup(OptionGroup.PET_FAMILY);
		Assert.assertNotNull(options);
		for (Option option : options) {
			logger.debug("-- {}", option);
		}
	}

}
