package net.ipetty.core.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础Controller类，提供基础功能，如Logger
 * 
 * @author luocanfeng
 * @date 2014年3月27日
 */
public abstract class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

}
