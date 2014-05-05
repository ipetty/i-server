package net.ipetty.core.web.rest;

import javax.servlet.http.HttpServletRequest;

import net.ipetty.core.web.rest.exception.UnknownResourceException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xiaojinghai
 */
@Controller
public class DefaultController extends BaseController {

	@RequestMapping("/**")
	public void unmappedRequest(HttpServletRequest request) {
		throw new UnknownResourceException("There is no resource for path " + request.getRequestURI());
	}

}
