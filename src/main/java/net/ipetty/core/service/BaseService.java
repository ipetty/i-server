package net.ipetty.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service基类
 * 
 * @author luocanfeng
 * @date 2014年5月4日
 */
@Transactional
public abstract class BaseService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

}
