package net.ipetty.core.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 所有实体的基类
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public abstract class AbstractEntity implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1167460074726231060L;

	private int version; // 实体版本号

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
