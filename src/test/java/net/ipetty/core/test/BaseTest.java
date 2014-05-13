package net.ipetty.core.test;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import net.ipetty.core.exception.BusinessException;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 基础测试类，提供基础功能，如Logger
 * 
 * @author luocanfeng
 * @date 2014年5月5日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public abstract class BaseTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String getTestPhotoPath() {
		return getPhotoPath("test.jpg");
	}

	protected String getPhotoPath(String photoFilename) {
		try {
			URL url = ClassLoader.getSystemResource(photoFilename);
			logger.debug("--photoPathURL={}", url);
			String photoPath = URLDecoder.decode(url.getPath(), "UTF-8");
			logger.debug("--photoPath={}", photoPath);
			return photoPath;
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(e);
		}
	}

}
