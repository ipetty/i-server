package net.ipetty.core.domain;

/**
 * 带Integer类型主键的实体类基类
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public abstract class IntegerIdEntity extends IdEntity<Integer> {

	/** serialVersionUID */
	private static final long serialVersionUID = -5174261440707200483L;

	public Integer getId() {
		return super.getId();
	}

	public void setId(Integer id) {
		super.setId(id);
	}

}
