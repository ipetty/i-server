package net.ipetty.core.web.rest.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 捕获并处理所有Controller异常
 * 
 * @author xiaojinghai
 */
@ControllerAdvice
public class CaughtExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(CaughtExceptionHandler.class);

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleMaxUploadSizeExceededError(RuntimeException e) {
		logger.error(e.getLocalizedMessage());
		return "文件过大";
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleRuntimeExceptionError(RuntimeException e) {
		e.printStackTrace();
		logger.error(e.getLocalizedMessage());
		return e.getLocalizedMessage();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String ExceptionError(Exception e) {
		e.printStackTrace();
		logger.error(e.getLocalizedMessage());
		return e.getLocalizedMessage();
	}

	// 所有异常
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleThrowableError(Throwable e) {
		e.printStackTrace();
		logger.error(e.getLocalizedMessage());
		return e.getLocalizedMessage();
	}

}
