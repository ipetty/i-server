package net.ipetty.foundation.service;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.service.BaseService;
import net.ipetty.foundation.repository.OptionDao;
import net.ipetty.vo.Option;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OptionService
 * 
 * @author luocanfeng
 * @date 2014年6月13日
 */
@Service
@Transactional
public class OptionService extends BaseService {

	@Resource
	private OptionDao optionDao;

	/**
	 * 获取指定选项组的所有选项
	 */
	public List<Option> listByGroup(String group) {
		return optionDao.listByGroup(group);
	}

}
