package net.ipetty.foundation.repository;

import java.util.List;

import net.ipetty.vo.Option;

/**
 * OptionDao
 * 
 * @author luocanfeng
 * @date 2014年6月13日
 */
public interface OptionDao {

	/**
	 * 获取指定选项组的所有选项
	 */
	public List<Option> listByGroup(String group);

}
