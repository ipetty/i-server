package net.ipetty.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 * SpringContextHelper
 * 
 * @author luocanfeng
 * @date 2014年5月5日
 */
@Service
public class SpringContextHelper implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	private static String webContextRealPath;

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 获取Web应用上下文实际绝对地址
	 */
	public static String getWebContextRealPath() {
		if (StringUtils.isEmpty(webContextRealPath)) {
			webContextRealPath = ((WebApplicationContext) applicationContext).getServletContext().getRealPath("/");
		}
		return webContextRealPath;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 在加载Spring时自动获得context
		SpringContextHelper.applicationContext = applicationContext;
	}

}
