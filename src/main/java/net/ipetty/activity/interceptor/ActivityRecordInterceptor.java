package net.ipetty.activity.interceptor;

import net.ipetty.activity.annotation.ProduceActivity;
import net.ipetty.activity.domain.Activity;
import net.ipetty.core.mq.ActivityHazelcastMQ;
import net.ipetty.core.util.AopUtils;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ActivityRecordInterceptor
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
@Component
@Aspect
public class ActivityRecordInterceptor {

	private final String PRODUCE_ACTIVITY = "@annotation(net.ipetty.activity.annotation.ProduceActivity)";

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 获取ActivityRecord，并进行相应业务处理
	 */
	@SuppressWarnings("unchecked")
	@Around(PRODUCE_ACTIVITY)
	public <T> T record(ProceedingJoinPoint call) throws Throwable {
		ProduceActivity activityAnnotation = AopUtils.getAnnotation(call, ProduceActivity.class);
		T result = (T) call.proceed();

		String targetIdKey = activityAnnotation.targetId();
		Long targetId = StringUtils.isNotBlank(targetIdKey) ? ((Number) AopUtils.executeSingleKey(targetIdKey, call,
				result)).longValue() : null;
		String createdByKey = activityAnnotation.createdBy();
		Integer createdBy = StringUtils.isNotBlank(createdByKey) ? (Integer) AopUtils.executeSingleKey(createdByKey,
				call, result) : null;
		Activity activity = new Activity(activityAnnotation.type(), targetId, createdBy);

		ActivityHazelcastMQ.publish(activity);
		logger.debug("published {}", activity);
		return result;
	}

}
