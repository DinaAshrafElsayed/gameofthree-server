package takeaway.server.gameofthree.exception;

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

	public UserDoesnotExistException() {
		super("user doesn't exist", HttpStatus.BAD_REQUEST);
	}
}
