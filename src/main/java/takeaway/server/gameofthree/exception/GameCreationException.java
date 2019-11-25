package takeaway.server.gameofthree.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * An exception thrown when a game can not be created
 * 
 * @author El-sayedD
 */
public class GameCreationException extends BusinessException {
	private static final long serialVersionUID = 1L;
	@Value("${GameCreationExceptionMessage}")
	private static String message;

	public GameCreationException() {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
