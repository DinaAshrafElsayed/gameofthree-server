package takeaway.server.gameofthree.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * An exception thrown when trying to retrieve a user using an email that isn't
 * registered
 * 
 * @author El-sayedD
 *
 */
public class UserDoesnotExistException extends BusinessException {
	private static final long serialVersionUID = -687991492884005033L;
	@Value("${userDoesntExistMessage}")
	private static String message;

	public UserDoesnotExistException() {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
