package net.ipetty.service;

import net.ipetty.core.test.BaseTestWithDBUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * AllServiceTest
 * 
 * @author luocanfeng
 * @date 2014年5月5日
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ UserServiceTest.class, PetServiceTest.class, LocationServiceTest.class, ImageServiceTest.class,
		FeedServiceTest.class })
public class AllServiceTest extends BaseTestWithDBUnit {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BaseTestWithDBUnit.setUpBeforeClass();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

}
