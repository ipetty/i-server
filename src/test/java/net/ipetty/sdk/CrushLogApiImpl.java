package net.ipetty.sdk;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;

/**
 * CrushLogApiImpl
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrushLogApiImpl extends BaseApi implements CrushLogApi {

	public CrushLogApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_SAVE_CRUSH_LOG = "/crush";

	/**
	 * 保存崩溃日志
	 */
	public boolean save(String log) {
		return context.getRestTemplate().postForObject(buildUri(URI_SAVE_CRUSH_LOG, "log", log), null, Boolean.class);
	}

}
