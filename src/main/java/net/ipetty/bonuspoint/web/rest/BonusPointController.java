package net.ipetty.bonuspoint.web.rest;

import javax.annotation.Resource;

import net.ipetty.bonuspoint.service.BonusPointService;
import net.ipetty.core.web.rest.BaseController;

import org.springframework.stereotype.Controller;

/**
 * BonusPointController
 * 
 * @author luocanfeng
 * @date 2014年6月9日
 */
@Controller
public class BonusPointController extends BaseController {

	@Resource
	private BonusPointService bonusPointService;

}
