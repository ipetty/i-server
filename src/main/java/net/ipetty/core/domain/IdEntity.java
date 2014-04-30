package net.ipetty.core.domain;

import java.util.Date;

/**
 * 带主键的实体类基类，含创建信息
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public abstract class IdEntity<T extends Object> extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -1636448041200188252L;

	private T id;
	private Integer createdBy;
	private Date createdOn;

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
