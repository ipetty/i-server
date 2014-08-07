package net.ipetty.sdk;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.CrashLogVO;

/**
 * CrashLogApiImpl
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrashLogApiImpl extends BaseApi implements CrashLogApi {

	public CrashLogApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_SAVE_CRASH_LOG = "/crash";

	/**
	 * 保存崩溃日志
	 */
	public boolean save(CrashLogVO crashLog) {
		return context.getRestTemplate().postForObject(buildUri(URI_SAVE_CRASH_LOG), crashLog, Boolean.class);
	}

}
