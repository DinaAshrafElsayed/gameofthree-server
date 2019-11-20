package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class GameCreationException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public GameCreationException() {
		super("failed to start game please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
