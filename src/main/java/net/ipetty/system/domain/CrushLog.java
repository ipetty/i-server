package net.ipetty.system.domain;

import net.ipetty.core.domain.AbstractEntity;

/**
 * 崩溃日志
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrushLog extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -4123629740281400212L;

	private String log;

	public CrushLog() {
		super();
	}

	public CrushLog(String log) {
		super();
		this.log = log;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
