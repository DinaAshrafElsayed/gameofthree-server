package takeaway.server.gameofthree.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import takeaway.server.gameofthree.dao.GameRepo;
import takeaway.server.gameofthree.dao.PlayerRepo;
import takeaway.server.gameofthree.dto.Game;
import takeaway.server.gameofthree.dto.GameInvitationStatusEnum;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.exception.GameCreationException;
import takeaway.server.gameofthree.exception.PlayerUnavailableException;
import takeaway.server.gameofthree.exception.UserDoesnotExistException;

/**
 * 
 * @author El-sayedD
 *
 */
@Service
public class GameOfThreeService {

	@Autowired
	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;

	@Autowired
	@Qualifier("GameRepoDefaultImpl")
	private GameRepo gameRepo;

	@Autowired
	private CommunicationService communicationService;

	public List<Player> getAvaliablePlayer() {
		//FIXME remove sender email from list
		return playerRepo.getAvaliablePlayers();
	}

	public GameInvitationStatusEnum startGame(String receiverEmail, int initalValue) throws BusinessException {
		String senderEmail = extractSenderEmailFromSecurityContext();
		Player receiver = retrievePlayerIfRegisteredAndAvailable(receiverEmail);
		GameInvitationStatusEnum status = sendGameInvitation(senderEmail, receiver);
		if (GameInvitationStatusEnum.ACCEPTED.equals(status)) {
			startGame(receiverEmail, senderEmail, initalValue);
		}
		return status;
	}

	private String extractSenderEmailFromSecurityContext() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String senderEmail = user.getUsername();
		return senderEmail;
	}

	private Player retrievePlayerIfRegisteredAndAvailable(String receiverEmail) throws BusinessException {
		Player receiver = playerRepo.findPlayerInRegisteryByEmail(receiverEmail);
		if (receiver == null) {
			throw new UserDoesnotExistException();
		} else if (!receiver.isAvailable()) {
			throw new PlayerUnavailableException();
		}
		return receiver;
	}

	private GameInvitationStatusEnum sendGameInvitation(String senderEmail, Player receiver) {
		return communicationService.sendGameInvitation(senderEmail, receiver);

	}

	private void startGame(String receiverEmail, String senderEmail, int initalValue) throws GameCreationException {
		boolean gameStarted = createNewGame(receiverEmail, senderEmail, initalValue);
		if (!gameStarted) {
			throw new GameCreationException();
		}
	}

	private boolean createNewGame(String receiverEmail, String senderEmail, int initalValue) {
		Game game = new Game();
		game.setCurrentValue(initalValue);
		game.setPlayerOneEmail(senderEmail);
		game.setPlayerTwoEmail(receiverEmail);
		game.setLastPlayedBy(senderEmail);
		Player sender = playerRepo.findPlayerInRegisteryByEmail(senderEmail);
		Player receiver = playerRepo.findPlayerInRegisteryByEmail(receiverEmail);
		List<Player> updatedPlayers = updatePlayersWithGameIdAndAvailability(game, sender, receiver);
		playerRepo.updatePlayersList(updatedPlayers);
		return gameRepo.saveGame(game);
	}

	private List<Player> updatePlayersWithGameIdAndAvailability(Game game, Player sender, Player receiver) {
		List<Player> updated = new ArrayList<>(2);
		boolean currentPlayerAvailability = false;
		sender.setCurrentGameId(game.getGameId());
		sender.setAvailable(currentPlayerAvailability);
		receiver.setCurrentGameId(game.getGameId());
		receiver.setAvailable(currentPlayerAvailability);
		updated.add(sender);
		updated.add(receiver);
		return updated;
	}
}
