package net.ipetty.activity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ProduceActivity
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProduceActivity {

	String type(); // ActivityType

	String createdBy() default ""; // 事件操作人

	String targetId() default ""; // 事件关联对象ID，如关注人的ID，评论对象的ID

	String thisId() default ""; // 对象的ID

	String content() default ""; // 内容，目前仅在回复事件时才有内容值

}
