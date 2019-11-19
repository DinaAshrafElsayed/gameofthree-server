package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * General Business exception, should be extended by any custom exception and
 * has its own handler in GeneralExceptionHanler.java
 */
@Data
public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;

	protected String message;
	protected Integer code;
	protected HttpStatus httpStatus;
	
	public BusinessException() {

	}

	public BusinessException(String message) {
		this.message = message;
	}

	public BusinessException(String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;
	}


	public BusinessException(int code, String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.code = code;
		this.httpStatus = httpStatus;
	}


}
