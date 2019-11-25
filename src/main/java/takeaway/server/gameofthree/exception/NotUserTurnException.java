package takeaway.server.gameofthree.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * An exception thrown when a player is not playing in his turn
 * 
 * @author El-sayedD
 *
 */
public class NotUserTurnException extends BusinessException {

	private static final long serialVersionUID = -794983855223820290L;
	
	@Value("${NotUserTurnExceptionMessage}")
	private static String message;

	public NotUserTurnException() {
		super(message, HttpStatus.BAD_REQUEST);
	}

}
