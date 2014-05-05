package net.ipetty.core.domain;

import java.util.Date;

/**
 * 带审计信息（最后更新信息）及主键的实体类基类
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public abstract class AuditableIdEntity<T extends Object> extends IdEntity<T> {

	/** serialVersionUID */
	private static final long serialVersionUID = 8949941035592252469L;

	private Integer lastModifiedBy;
	private Date lastModifiedOn;

	public void preUpdate() {
		this.setLastModifiedOn(new Date());
	}

	public Integer getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

}
