package net.ipetty.feed.domain;

import net.ipetty.core.domain.AuditableIntegerIdEntity;

/**
 * 标签
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
public class FeedLabel extends AuditableIntegerIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -2456865100979223214L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
