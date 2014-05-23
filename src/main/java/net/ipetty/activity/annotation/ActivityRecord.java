package net.ipetty.activity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ActivityRecord
 * 
 * @author luocanfeng
 * @date 2014年5月23日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActivityRecord {

	String type(); // ActivityType

	String createdBy() default "";

	String targetId() default "";

}
