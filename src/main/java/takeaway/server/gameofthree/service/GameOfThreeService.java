package takeaway.server.gameofthree.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import takeaway.server.gameofthree.dao.GameRepo;
import takeaway.server.gameofthree.dao.PlayerRepo;
import takeaway.server.gameofthree.dto.Player;

/**
 * 
 * @author El-sayedD
 *
 */
@Service
public class GameOfThreeService {

	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;

	@Qualifier("GameRepoDefaultImpl")
	private GameRepo gameRepo;
	
	@Autowired
	private CommunicationService communicationService;

	public List<Player> getAvaliablePlayer() {
		return playerRepo.getAvaliablePlayers();
	}

	public void startGame(String playerTwoEmail, String initalValue) {
		boolean playerIsAvaliable = playerRepo.isPlayerAvaliable(playerTwoEmail);
		if(playerIsAvaliable) {
			UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String senderEmail = user.getUsername();
			Player receiver = playerRepo.findPlayerInRegistery(playerTwoEmail);
			boolean accepted = communicationService.AcceptGameInvitation(senderEmail, receiver);
			// send accept Request to player2
			if(accepted) {
				// if accepts save new game
				// save player1, player2 gameId
			}else {
				// else send decline and try again
			}
			
		}else {
			//TODO try again
		}
		

	}
}
