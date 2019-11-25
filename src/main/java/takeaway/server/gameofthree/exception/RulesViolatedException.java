package takeaway.server.gameofthree.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * An exception thrown when a new played value doesn't follow the rules of the
 * game
 * 
 * @author El-sayedD
 *
 */
public class RulesViolatedException extends BusinessException {

	private static final long serialVersionUID = 1075948290075257035L;
	
	@Value("${RulesViolatedExceptionMessage}")
	private static String message;

	public RulesViolatedException() {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
