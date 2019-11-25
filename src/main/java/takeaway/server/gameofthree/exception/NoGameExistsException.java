package takeaway.server.gameofthree.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * 
 * An exception thrown when a game with a specific ID can not be found
 * 
 * @author El-sayedD
 */
public class NoGameExistsException extends BusinessException {
	private static final long serialVersionUID = -4022903937865180922L;
	@Value("${NoGameExistsExceptionMessage}")
	private static String message;

	public NoGameExistsException() {
		super(message, HttpStatus.BAD_REQUEST);
	}

}
