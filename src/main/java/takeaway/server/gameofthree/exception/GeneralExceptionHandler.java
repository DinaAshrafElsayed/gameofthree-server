package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import takeaway.server.gameofthree.dto.Error;
/**
 * 
 * @author El-sayedD
 *
 */

@RestControllerAdvice
@EnableWebMvc
@Slf4j
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseBody
	public ResponseEntity<Error> handleExpiredJwtException(ExpiredJwtException e, WebRequest request) {
		log.error(e.getMessage(), e);
		Error errorResponse = new Error();
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(HttpStatus.FORBIDDEN.value());

		return new ResponseEntity<Error>(errorResponse, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public ResponseEntity<Error> processMethodNotSupportedException(BusinessException e, WebRequest request) {
		log.error(e.getMessage(), e);
		Error errorResponse = new Error();
		errorResponse.setMessage(e.getMessage());
		errorResponse.setCode(e.getCode());

		if (e.getHttpStatus() == null) {
			errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return new ResponseEntity<Error>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		errorResponse.setStatus(e.getHttpStatus().value());

		return new ResponseEntity<Error>(errorResponse, e.getHttpStatus());
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseBody
	public ResponseEntity<Error> procesAuthenticationException(AuthenticationException e, WebRequest request) {
		log.error(e.getMessage(), e);
		Error errorResponse = new Error();
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(HttpStatus.FORBIDDEN.value());

		return new ResponseEntity<Error>(errorResponse, HttpStatus.FORBIDDEN);

	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	public ResponseEntity<Error> processAccessDeniedException(AccessDeniedException e, WebRequest request) {
		log.error(e.getMessage(), e);
		Error errorResponse = new Error();
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		errorResponse.setCode(HttpStatus.UNAUTHORIZED.value());

		return new ResponseEntity<Error>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Error> processRuntimeException(Exception e, WebRequest request) {
		log.error(e.getMessage(), e);
		StackTraceElement[] stacktraceArray = e.getStackTrace();
		StringBuilder detailedException = new StringBuilder(e.getMessage() + "\n");
		for (StackTraceElement element : stacktraceArray) {
			detailedException.append("Line number: " + element.getLineNumber() + ", ");
			detailedException.append("method name: " + element.getMethodName() + ", ");
			detailedException.append("Class name: " + element.getClassName() + ". \n");
		}
		log.error(detailedException.toString());

		Error errorResponse = new Error();
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<Error>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResponseEntity<Error> handle(Throwable e, WebRequest request) {
		log.error(e.getMessage(), e);
		Error errorResponse = new Error();
		errorResponse.setMessage(e.getMessage());
		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		return new ResponseEntity<Error>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
