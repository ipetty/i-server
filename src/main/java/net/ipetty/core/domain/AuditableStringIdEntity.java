package net.ipetty.core.domain;

/**
 * 带审计信息（最后更新信息）及String类型主键的实体类基类
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public abstract class AuditableStringIdEntity extends AuditableIdEntity<String> {

	/** serialVersionUID */
	private static final long serialVersionUID = 7560977936768555335L;

}
