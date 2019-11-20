package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class PlayerUnavailableException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public PlayerUnavailableException() {
		super("player is in another game", HttpStatus.CONFLICT);
	}
}
