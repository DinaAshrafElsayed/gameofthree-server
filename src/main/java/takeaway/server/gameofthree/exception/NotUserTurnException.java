package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class NotUserTurnException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -794983855223820290L;

	public NotUserTurnException() {
		super("not your turn to play please wait for other player", HttpStatus.BAD_REQUEST);
	}

}