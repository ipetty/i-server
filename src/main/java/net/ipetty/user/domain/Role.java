package net.ipetty.user.domain;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.core.domain.AuditableIntegerIdEntity;

/**
 * 角色
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class Role extends AuditableIntegerIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -6907739680157697119L;

	private List<Privilege> prifileges = new ArrayList<Privilege>(); // 权限列表

	public List<Privilege> getPrifileges() {
		return prifileges;
	}

	public void setPrifileges(List<Privilege> prifileges) {
		this.prifileges = prifileges;
	}

}
