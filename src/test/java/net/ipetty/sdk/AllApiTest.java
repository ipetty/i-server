package net.ipetty.sdk;

import net.ipetty.core.test.BaseTestWithDBUnit;
import net.ipetty.core.test.JettyServer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * AllApiTest
 * 
 * @author luocanfeng
 * @date 2014年5月6日
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ UserApiTest.class, FeedApiTest.class, FeedbackApiTest.class, ActivityApiTest.class })
public class AllApiTest extends BaseTestWithDBUnit {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BaseTestWithDBUnit.setUpBeforeClass();
		JettyServer.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		JettyServer.stop();
	}

}
