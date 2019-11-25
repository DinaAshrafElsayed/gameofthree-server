package takeaway.server.gameofthree.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import takeaway.server.gameofthree.dao.PlayerRepo;
import takeaway.server.gameofthree.util.GameOfThreeUtil;

/**
 * this service is responsible for getting available Players
 * 
 * @author El-sayedD
 *
 */
@Service
public class AvailablePlayersService {
	@Autowired
	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;
	
	@Autowired
	private GameOfThreeUtil gameOfThreeUtil;
	/**
	 * Retrieves list of players that are available to start a new game (not busy
	 * with any current game)
	 * 
	 * @return list of available players
	 */
	public Set<String> getAvaliablePlayer() {
		String senderEmail = gameOfThreeUtil.extractSenderEmailFromSecurityContext();
		return playerRepo.getAvailablePlayersEmailNotEqualEmail(senderEmail);
	}

}
