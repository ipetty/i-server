package net.ipetty.foundation.web.rest;

import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.web.rest.BaseController;
import net.ipetty.foundation.service.OptionService;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * FoundationController
 * 
 * @author luocanfeng
 * @date 2014年6月13日
 */
@Controller
public class FoundationController extends BaseController {

	@Resource
	private OptionService optionService;

	/**
	 * 获取指定选项组的所有选项
	 */
	@RequestMapping(value = "/options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Option> listOptionsByGroup(String group) {
		Assert.hasText(group, "选项组不能为空");
		Assert.isTrue(OptionGroup.ALL.contains(group), "非法的选项组");

		return optionService.listByGroup(group);
	}

}
