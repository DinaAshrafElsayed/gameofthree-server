package takeaway.server.gameofthree.exception;

import org.springframework.http.HttpStatus;

public class RulesViolatedException extends BusinessException {

	private static final long serialVersionUID = 1075948290075257035L;

	public RulesViolatedException() {
		super("rules not applied exception please try again", HttpStatus.BAD_REQUEST);
	}
}
