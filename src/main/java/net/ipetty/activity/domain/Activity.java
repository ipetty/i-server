package net.ipetty.activity.domain;

import java.util.Date;

import net.ipetty.core.domain.LongIdEntity;
import net.ipetty.vo.ActivityVO;

import org.springframework.beans.BeanUtils;

/**
 * 事件
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Activity extends LongIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -3528508014802614988L;

	private String type; // ActivityType
	private Long targetId; // 受影响的目标ID
	private Long thisId; // 当前操作的实体ID
	private String content; // 内容，目前仅在回复事件时才有内容值

	public Activity() {
		super();
	}

	public Activity(String type, Date createdOn) {
		super();
		this.type = type;
		setCreatedOn(createdOn);
	}

	public Activity(String type) {
		super();
		this.type = type;
		setCreatedOn(new Date());
	}

	public Activity(String type, Integer createdBy, Date createdOn) {
		super();
		this.type = type;
		setCreatedBy(createdBy);
		setCreatedOn(createdOn);
	}

	public Activity(String type, Integer createdBy) {
		super();
		this.type = type;
		setCreatedBy(createdBy);
		setCreatedOn(new Date());
	}

	public Activity(String type, Long targetId, Integer createdBy, Date createdOn) {
		super();
		this.type = type;
		this.targetId = targetId;
		setCreatedBy(createdBy);
		setCreatedOn(createdOn);
	}

	public Activity(String type, Long targetId, Integer createdBy) {
		super();
		this.type = type;
		this.targetId = targetId;
		setCreatedBy(createdBy);
		setCreatedOn(new Date());
	}

	public Activity(String type, Long targetId, String content, Integer createdBy, Date createdOn) {
		super();
		this.type = type;
		this.targetId = targetId;
		this.content = content;
		setCreatedBy(createdBy);
		setCreatedOn(createdOn);
	}

	public Activity(String type, Long targetId, String content, Integer createdBy) {
		super();
		this.type = type;
		this.targetId = targetId;
		this.content = content;
		setCreatedBy(createdBy);
		setCreatedOn(new Date());
	}

	public ActivityVO toVO() {
		ActivityVO vo = new ActivityVO();
		BeanUtils.copyProperties(this, vo);
		return vo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getThisId() {
		return thisId;
	}

	public void setThisId(Long thisId) {
		this.thisId = thisId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
