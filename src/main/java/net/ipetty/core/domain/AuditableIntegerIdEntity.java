package net.ipetty.core.domain;

/**
 * 带审计信息（最后更新信息）及Integer类型主键的实体类基类
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public abstract class AuditableIntegerIdEntity extends AuditableIdEntity<Integer> {

	/** serialVersionUID */
	private static final long serialVersionUID = 4272561667244009568L;

}
