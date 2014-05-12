package net.ipetty.core.domain;

/**
 * 带Long类型主键的实体类基类
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public abstract class LongIdEntity extends IdEntity<Long> {

	/** serialVersionUID */
	private static final long serialVersionUID = 8159012266123229353L;

	public Long getId() {
		return super.getId();
	}

	public void setId(Long id) {
		super.setId(id);
	}

}
