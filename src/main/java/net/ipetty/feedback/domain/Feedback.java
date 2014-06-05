package net.ipetty.feedback.domain;

import net.ipetty.core.domain.IntegerIdEntity;

/**
 * 意见反馈
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Feedback extends IntegerIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -4608384483398619692L;

	private String title; // 主题
	private String content; // 反馈内容
	private String contact; // 联系方式，非必填，用户未登录的情况下反馈意见时可留下联系方式

	public Feedback() {
		super();
	}

	public Feedback(String title, String content, String contact, Integer createdBy) {
		super();
		this.title = title;
		this.content = content;
		this.contact = contact;
		super.setCreatedBy(createdBy);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

}
