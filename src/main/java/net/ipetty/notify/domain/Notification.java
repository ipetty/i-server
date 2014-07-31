package net.ipetty.notify.domain;

import java.util.Date;

import net.ipetty.core.domain.IntegerIdEntity;
import net.ipetty.vo.NotificationVO;

import org.springframework.beans.BeanUtils;

/**
 * 用户通知统计对象
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Notification extends IntegerIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -7477247117515635917L;

	private Integer userId;
	private int newFansNum; // 新粉丝数目
	private Date newFansLastCheckDatetime; // 最后一次查看新粉丝的时间点
	private int newRepliesNum; // 新回复数目
	private Date newRepliesLastCheckDatetime; // 最后一次查看新回复的时间点
	private int newFavorsNum; // 新赞数目
	private Date newFavorsLastCheckDatetime; // 最后一次查看新赞的时间点

	public Notification() {
		super();
	}

	public Notification(Integer userId, int newFansNum, Date newFansLastCheckDatetime, int newRepliesNum,
			Date newRepliesLastCheckDatetime, int newFavorsNum, Date newFavorsLastCheckDatetime) {
		super();
		this.userId = userId;
		this.newFansNum = newFansNum;
		this.newFansLastCheckDatetime = newFansLastCheckDatetime;
		this.newRepliesNum = newRepliesNum;
		this.newRepliesLastCheckDatetime = newRepliesLastCheckDatetime;
		this.newFavorsNum = newFavorsNum;
		this.newFavorsLastCheckDatetime = newFavorsLastCheckDatetime;
	}

	public NotificationVO toVO() {
		NotificationVO vo = new NotificationVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getNewFansNum() {
		return newFansNum;
	}

	public void setNewFansNum(int newFansNum) {
		this.newFansNum = newFansNum;
	}

	public Date getNewFansLastCheckDatetime() {
		return newFansLastCheckDatetime;
	}

	public void setNewFansLastCheckDatetime(Date newFansLastCheckDatetime) {
		this.newFansLastCheckDatetime = newFansLastCheckDatetime;
	}

	public int getNewRepliesNum() {
		return newRepliesNum;
	}

	public void setNewRepliesNum(int newRepliesNum) {
		this.newRepliesNum = newRepliesNum;
	}

	public Date getNewRepliesLastCheckDatetime() {
		return newRepliesLastCheckDatetime;
	}

	public void setNewRepliesLastCheckDatetime(Date newRepliesLastCheckDatetime) {
		this.newRepliesLastCheckDatetime = newRepliesLastCheckDatetime;
	}

	public int getNewFavorsNum() {
		return newFavorsNum;
	}

	public void setNewFavorsNum(int newFavorsNum) {
		this.newFavorsNum = newFavorsNum;
	}

	public Date getNewFavorsLastCheckDatetime() {
		return newFavorsLastCheckDatetime;
	}

	public void setNewFavorsLastCheckDatetime(Date newFavorsLastCheckDatetime) {
		this.newFavorsLastCheckDatetime = newFavorsLastCheckDatetime;
	}

}
