package takeaway.server.gameofthree.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * An exception thrown when a player is not available to start a new game right
 * now
 * 
 * @author El-sayedD
 *
 */
public class PlayerUnavailableException extends BusinessException {
	private static final long serialVersionUID = 1L;
	
	@Value("${PlayerUnavailableExceptionMessage}")
	private static String message;

	public PlayerUnavailableException() {
		super(message, HttpStatus.CONFLICT);
	}
}
