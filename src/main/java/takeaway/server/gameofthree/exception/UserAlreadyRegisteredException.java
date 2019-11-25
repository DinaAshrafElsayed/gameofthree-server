package takeaway.server.gameofthree.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * An exception thrown when trying to register an already registered user
 * 
 * @author El-sayedD
 *
 */
public class UserAlreadyRegisteredException extends BusinessException {

	private static final long serialVersionUID = 1L;

	@Value("${userAlreadyRegisteredMessage}")
	private static String message;
	
	public UserAlreadyRegisteredException() {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
