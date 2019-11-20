package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class UserDoesnotExistExecption extends BusinessException {
	private static final long serialVersionUID = 1L;

	public UserDoesnotExistExecption() {
		super("user doesn't exist", HttpStatus.BAD_REQUEST);
	}
}
