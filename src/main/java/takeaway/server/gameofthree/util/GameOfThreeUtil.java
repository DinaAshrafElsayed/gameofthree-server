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

@Component
public class GameOfThreeUtil {
	
	@Autowired
	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;

	public String extractSenderEmailFromSecurityContext() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String senderEmail = user.getUsername();
		return senderEmail;
	}

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
