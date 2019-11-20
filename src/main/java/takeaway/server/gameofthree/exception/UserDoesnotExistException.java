package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class UserDoesnotExistException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public UserDoesnotExistException() {
		super("user doesn't exist", HttpStatus.BAD_REQUEST);
	}
}
