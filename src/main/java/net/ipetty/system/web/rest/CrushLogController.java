package net.ipetty.system.web.rest;

import java.io.IOException;

import javax.annotation.Resource;

import net.ipetty.core.web.rest.BaseController;
import net.ipetty.system.service.CrushLogService;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * CrushLogController
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
@Controller
public class CrushLogController extends BaseController {

	@Resource
	private CrushLogService crushLogService;

	/**
	 * 保存崩溃日志
	 */
	@RequestMapping(value = "/crush", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean save(String log) throws IOException {
		Assert.notNull(log, "崩溃日志不能为空");
		crushLogService.save(log);
		return true;
	}

}
