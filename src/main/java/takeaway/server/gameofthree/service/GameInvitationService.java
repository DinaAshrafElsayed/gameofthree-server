package takeaway.server.gameofthree.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import takeaway.server.gameofthree.dao.GameRepo;
import takeaway.server.gameofthree.dao.PlayerRepo;
import takeaway.server.gameofthree.dto.Game;
import takeaway.server.gameofthree.dto.GameInvitationStatusEnum;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.exception.GameCreationException;
import takeaway.server.gameofthree.util.GameOfThreeUtil;

/**
 * this service is responsible for sending an invitation to another player and
 * creating a game Id to link both players
 * 
 * @author El-sayedD
 *
 */
@Service
public class GameInvitationService {

	@Autowired
	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;

	@Autowired
	@Qualifier("GameRepoDefaultImpl")
	private GameRepo gameRepo;

	@Autowired
	private CommunicationService communicationService;

	@Autowired
	private GameOfThreeUtil gameOfThreeUtil;

	/**
	 * Retrieves list of players that are available to start a new game (not busy
	 * with any current game)
	 * 
	 * @return list of available players
	 */
	public List<Player> getAvaliablePlayer() {
		/* FIXME remove sender email from list */
		return playerRepo.getAvaliablePlayers();
	}

	/**
	 * Used by a player to start a new game with another player
	 * 
	 * @param receiverEmail the player to be invited to a new game
	 * @param initalValue   the initial value to start a new game
	 * @return Result of invitation either ACCEPTED or DECLINED
	 * @throws BusinessException either UserDoesnotExistException if player's email
	 *                           doesn't exist or PlayerUnavailableException if
	 *                           player is not available for a new game
	 */
	public GameInvitationStatusEnum startGame(String receiverEmail, int initalValue) throws BusinessException {
		/* TODO make sure receiver and sender are not same person */
		String senderEmail = gameOfThreeUtil.extractSenderEmailFromSecurityContext();
		Player receiver = gameOfThreeUtil.retrievePlayerIfRegisteredAndAvailable(receiverEmail);
		GameInvitationStatusEnum status = sendGameInvitation(senderEmail, receiver);
		if (GameInvitationStatusEnum.ACCEPTED.equals(status)) {
			startGame(receiverEmail, senderEmail, initalValue);
		}
		return status;
	}

	/**
	 * Sending invitation to another player to start a new game
	 * 
	 * @param senderEmail email of the player sending the invitation
	 * @param receiver    the player receiving the invitation
	 * @return Result of invitation either ACCEPTED or DECLINED
	 */
	private GameInvitationStatusEnum sendGameInvitation(String senderEmail, Player receiver) {
		return communicationService.sendGameInvitation(senderEmail, receiver);

	}

	/**
	 * Starting a new game after the invitation is ACCEPTED
	 * 
	 * @param receiverEmail the player invited to the game
	 * @param senderEmail   the player sending the invitation
	 * @param initalValue   initial value of the game
	 * @throws GameCreationException if anything goes wrong with Game Creation
	 */
	private void startGame(String receiverEmail, String senderEmail, int initalValue) throws GameCreationException {
		boolean gameStarted = createNewGame(receiverEmail, senderEmail, initalValue);
		if (!gameStarted) {
			throw new GameCreationException();
		}
	}

	/**
	 * Does the actual creation of the game and updating players with game ID
	 * 
	 * @param receiverEmail the player sending the invitation
	 * @param senderEmail   the player receiving the invitation
	 * @param initalValue   the initial value of the game
	 * @return true if game is created, false otherwise
	 */
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

	/**
	 * Updating the player s with game ID that they are currently playing
	 * 
	 * @param game     the running game
	 * @param sender   the first player
	 * @param receiver the second player
	 * @return list of the two players after updating the game id
	 */
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
