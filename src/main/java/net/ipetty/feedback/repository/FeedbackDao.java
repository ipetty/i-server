package net.ipetty.feedback.repository;

import net.ipetty.feedback.domain.Feedback;

/**
 * FeedbackDao
 * 
 * @author luocanfeng
 * @date 2014年6月5日
 */
public interface FeedbackDao {

	/**
	 * 保存
	 */
	public void save(Feedback feedback);

}
