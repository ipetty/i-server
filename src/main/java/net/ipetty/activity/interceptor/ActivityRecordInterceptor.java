package net.ipetty.activity.interceptor;

import net.ipetty.activity.annotation.ActivityRecord;
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

	private final String ACTIVITY_RECORD = "@annotation(net.ipetty.activity.annotation.ActivityRecord)";

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 获取ActivityRecord，并进行相应业务处理
	 */
	@SuppressWarnings("unchecked")
	@Around(ACTIVITY_RECORD)
	public <T> T get(ProceedingJoinPoint call) throws Throwable {
		ActivityRecord activityRecord = AopUtils.getAnnotation(call, ActivityRecord.class);
		T result = (T) call.proceed();

		String targetIdKey = activityRecord.targetId();
		Long targetId = StringUtils.isNotBlank(targetIdKey) ? ((Number) AopUtils.executeOgnl(targetIdKey, call, result))
				.longValue() : null;
		String createdByKey = activityRecord.createdBy();
		Integer createdBy = StringUtils.isNotBlank(createdByKey) ? (Integer) AopUtils.executeOgnl(createdByKey, call,
				result) : null;
		Activity activity = new Activity(activityRecord.type(), targetId, createdBy);

		ActivityHazelcastMQ.publish(activity);
		logger.debug("published {}", activity);
		return result;
	}

}
