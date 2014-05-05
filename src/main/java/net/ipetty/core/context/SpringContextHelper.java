package net.ipetty.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * SpringContextHelper
 * 
 * @author luocanfeng
 * @date 2014年5月5日
 */
@Service
public class SpringContextHelper implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return getBean(clazz.getName());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 在加载Spring时自动获得context
		SpringContextHelper.applicationContext = applicationContext;
	}

}
