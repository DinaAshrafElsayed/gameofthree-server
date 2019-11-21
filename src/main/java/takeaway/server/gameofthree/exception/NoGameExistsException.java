package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class NoGameExistsException extends BusinessException {
	private static final long serialVersionUID = -4022903937865180922L;

	public NoGameExistsException() {
		super("please start a new Game", HttpStatus.BAD_REQUEST);
	}

}
