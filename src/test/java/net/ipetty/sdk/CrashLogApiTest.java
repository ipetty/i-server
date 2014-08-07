package net.ipetty.sdk;

import net.ipetty.core.test.BaseApiTest;
import net.ipetty.sdk.common.ApiContext;
import net.ipetty.vo.CrashLogVO;

import org.junit.Assert;
import org.junit.Test;

/**
 * CrashLogApiTest
 * 
 * @author luocanfeng
 * @date 2014年8月5日
 */
public class CrashLogApiTest extends BaseApiTest {

	CrashLogApi crashLogApi = new CrashLogApiImpl(ApiContext.getInstance());

	@Test
	public void testSave() {
		Assert.assertTrue(crashLogApi.save(new CrashLogVO(null, "user", "4.1.2", 8, "1.0.8", "crash",
				"test crash log from api test")));
	}

}
