package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyRegisteredException extends BusinessException{

	private static final long serialVersionUID = 1L;

	public UserAlreadyRegisteredException() {
		super("user already registered", HttpStatus.BAD_REQUEST);
	}
}
