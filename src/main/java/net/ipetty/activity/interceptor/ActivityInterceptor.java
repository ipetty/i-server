package net.ipetty.activity.interceptor;

import javax.annotation.Resource;

import net.ipetty.activity.annotation.ProduceActivity;
import net.ipetty.activity.domain.Activity;
import net.ipetty.activity.mq.ActivityMQMessageSender;
import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.util.AopUtils;
import ognl.OgnlException;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ActivityInterceptor
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
@Component
@Aspect
public class ActivityInterceptor {

	private final String PRODUCE_ACTIVITY = "@annotation(net.ipetty.activity.annotation.ProduceActivity)";

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private ActivityMQMessageSender activityMQMessageSender;

	/**
	 * 获取ActivityRecord，并进行相应业务处理
	 */
	@SuppressWarnings("unchecked")
	@Around(PRODUCE_ACTIVITY)
	public <T> T record(ProceedingJoinPoint call) throws Throwable {
		ProduceActivity activityAnnotation = AopUtils.getAnnotation(call, ProduceActivity.class);
		T result = (T) call.proceed();

		try {
			String targetIdKey = activityAnnotation.targetId();
			String targetIdStr = AopUtils.executeOgnlKey(targetIdKey, call, result);
			Long targetId = StringUtils.isNotBlank(targetIdStr) ? Long.valueOf(targetIdStr) : null;
			String thisIdKey = activityAnnotation.thisId();
			String thisIdStr = AopUtils.executeOgnlKey(thisIdKey, call, result);
			Long thisId = StringUtils.isNotBlank(thisIdStr) ? Long.valueOf(thisIdStr) : null;
			String contentKey = activityAnnotation.content();
			String createdByKey = activityAnnotation.createdBy();
			String createdByStr = AopUtils.executeOgnlKey(createdByKey, call, result);
			Integer createdBy = null;
			if (StringUtils.isNotBlank(createdByStr)) {
				createdBy = Integer.valueOf(createdByStr);
			} else {
				UserPrincipal principal = UserContext.getContext();
				logger.debug("-----{}", principal);
				createdBy = principal == null ? null : principal.getId();
			}
			Activity activity = new Activity(activityAnnotation.type(), targetId, createdBy);
			if (StringUtils.isNotBlank(contentKey)) {
				String content = AopUtils.executeOgnlKey(contentKey, call, result);
				activity.setContent(content);
			}
			activity.setThisId(thisId);
			activityMQMessageSender.publish(activity);
			logger.debug("published {}", activity);
		} catch (OgnlException e) {
			logger.error("Ognl Execute Execption: ", e);
		}

		return result;
	}

}
