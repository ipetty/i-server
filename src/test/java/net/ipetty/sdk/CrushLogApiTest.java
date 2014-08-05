package net.ipetty.sdk;

import net.ipetty.core.test.BaseApiTest;
import net.ipetty.sdk.common.ApiContext;

import org.junit.Assert;
import org.junit.Test;

/**
 * CrushLogApiTest
 * 
 * @author luocanfeng
 * @date 2014年8月5日
 */
public class CrushLogApiTest extends BaseApiTest {

	CrushLogApi crushLogApi = new CrushLogApiImpl(ApiContext.getInstance());

	@Test
	public void testSave() {
		Assert.assertTrue(crushLogApi.save("test crush log from api test"));
	}

}
