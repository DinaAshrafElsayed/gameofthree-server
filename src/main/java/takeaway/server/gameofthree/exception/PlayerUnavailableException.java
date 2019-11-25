package takeaway.server.gameofthree.exception;

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

	public PlayerUnavailableException() {
		super("player is in another game", HttpStatus.CONFLICT);
	}
}
