package takeaway.server.gameofthree.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import takeaway.server.gameofthree.dao.PlayerRepo;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.exception.PlayerUnavailableException;
import takeaway.server.gameofthree.exception.UserDoesnotExistException;

/**
 * Utils and helper functions
 * 
 * @author El-sayedD
 */
@Component
public class GameOfThreeUtil {

	@Autowired
	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;

	/**
	 * Extracting an email from security context
	 * 
	 * @return the user email
	 */
	public String extractSenderEmailFromSecurityContext() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String senderEmail = user.getUsername();
		return senderEmail;
	}

	/**
	 * Retrieving player from playerRepo if and only if user exists and is available
	 * for a new game
	 * 
	 * @param receiverEmail email of the player to be retrieved
	 * @return the queried player
	 * @throws BusinessException either UserDoesnotExistException if player's email
	 *                           doesn't exist or PlayerUnavailableException if
	 *                           player is not available for a new game
	 */
	public Player retrievePlayerIfRegisteredAndAvailable(String receiverEmail) throws BusinessException {
		Player receiver = playerRepo.findPlayerInRegisteryByEmail(receiverEmail);
		if (receiver == null) {
			throw new UserDoesnotExistException();
		} else if (!receiver.isAvailable()) {
			throw new PlayerUnavailableException();
		}
		return receiver;
	}
}
