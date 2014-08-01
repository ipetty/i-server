package net.ipetty.sdk;

import net.ipetty.sdk.common.ApiContext;
import net.ipetty.sdk.common.BaseApi;
import net.ipetty.vo.NotificationVO;

/**
 * NotificationApiImpl
 * 
 * @author luocanfeng
 * @date 2014年8月1日
 */
public class NotificationApiImpl extends BaseApi implements NotificationApi {

	public NotificationApiImpl(ApiContext context) {
		super(context);
	}

	private static final String URI_GET_MY_NOTIFICATION = "/notification";

	/**
	 * 获取我的通知
	 */
	public NotificationVO getMyNotification() {
		super.requireAuthorization();

		return context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + URI_GET_MY_NOTIFICATION,
				NotificationVO.class);
	}

}
