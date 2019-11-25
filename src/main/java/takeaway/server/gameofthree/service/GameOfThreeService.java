package takeaway.server.gameofthree.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import takeaway.server.gameofthree.dao.GameRepo;
import takeaway.server.gameofthree.dao.PlayerRepo;
import takeaway.server.gameofthree.dto.Game;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.exception.NoGameExistsException;
import takeaway.server.gameofthree.exception.NotUserTurnException;
import takeaway.server.gameofthree.exception.RulesViolatedException;
import takeaway.server.gameofthree.util.GameOfThreeUtil;

/**
 * this service is responsible for play operation and it's validation
 * 
 * @author El-sayedD
 *
 */
@Service
public class GameOfThreeService {

	@Autowired
	private CommunicationService communicationService;

	@Autowired
	private GameOfThreeUtil gameOfThreeUtil;

	@Autowired
	@Qualifier("GameRepoDefaultImpl")
	private GameRepo gameRepo;

	@Autowired
	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;

	/**
	 * sending a new value during the game, the player id is retrieved from the
	 * token and then the game id is retrieved from the player object
	 * 
	 * @param value new value of game
	 * @throw NoGameExistsException in case that user has no ongoing game
	 * 
	 * @throws NotUserTurnException     in case that user is sending request and
	 *                                  it's not his turn yet
	 * @throws RulesNotAppliedException in case rule not matched make sure game is
	 *                                  still in progress no winner announced yet(
	 *                                  value !=1) validate input from previous one
	 *                                  (matches rule (-1,0,1)) and divisible by 3
	 * 
	 */
	public void play(int value) throws BusinessException {
		String senderEmail = gameOfThreeUtil.extractSenderEmailFromSecurityContext();
		Player sender = playerRepo.findPlayerInRegisteryByEmail(senderEmail);
		checkIfPlayerHasAnOngoingGame(sender);
		Game game = gameRepo.findGameById(sender.getCurrentGameId());
		/* not first play */
		if(game.getLastPlayedBy() != null && !game.getLastPlayedBy().equals("")) {
			checkIfItIsPlayerTurn(game, senderEmail);
			applyRules(game, value);
			value /= 3;
		}
		boolean playerOneWon = value == 1 ? true : false;
		
		/*
		 * TODO handle win case else normal case
		 */
		String playerTwoEmail = getPlayerTwoEmail(senderEmail, game);
		Player playerTwo = playerRepo.findPlayerInRegisteryByEmail(playerTwoEmail);
		if (playerTwo != null) {
			communicationService.sendNewValue(playerTwo, value);
		} else {
			/* TODO return playerTwo withdraw return you won */
		}
		/* TODO if game ended I should remove game and update players */
		updateGame(game, senderEmail, value);
	}

	/**
	 * Checking if player is currently in a running game
	 * 
	 * @param player the player to be checked
	 * @throws BusinessException if the player is in a running game
	 */
	private void checkIfPlayerHasAnOngoingGame(Player player) throws BusinessException {
		if (player.getCurrentGameId() == null || player.getCurrentGameId().equals("")) {
			throw new NoGameExistsException();
		}
	}

	/**
	 * checking if it's player turn to play
	 * 
	 * @param game        the running game
	 * @param senderEmail email of the player to be checked
	 * @throws BusinessException if it's not user's turn
	 */
	private void checkIfItIsPlayerTurn(Game game, String senderEmail) throws BusinessException {
		String lastPlayedBy = game.getLastPlayedBy();
		if (senderEmail.equals(lastPlayedBy)) {
			throw new NotUserTurnException();
		}
	}

	/**
	 * Making sure the newValue follow the rules of the game with respect to the
	 * latest value played
	 * 
	 * @param game     the running game
	 * @param newValue the new value
	 * @throws BusinessException if the newValue doesn't follow the rules of the
	 *                           game
	 */
	private void applyRules(Game game, int newValue) throws BusinessException {
		int previousValue = game.getCurrentValue();
		if (previousValue - newValue > 1 || previousValue - newValue < -1 && newValue % 3 != 0) {
			throw new RulesViolatedException();
		}
	}

	/**
	 * retrieving the other player email from the game object
	 * 
	 * @param senderEmail first player
	 * @param game        running game
	 * @return email of the other player
	 */
	private String getPlayerTwoEmail(String senderEmail, Game game) {
		String playerTwo;
		if (senderEmail.equals(game.getPlayerOneEmail())) {
			playerTwo = game.getPlayerTwoEmail();
		} else {
			playerTwo = game.getPlayerOneEmail();
		}
		return playerTwo;
	}

	/**
	 * updating the game after a new value is submitted
	 * 
	 * @param game        the game
	 * @param senderEmail last player
	 * @param newValue    the newValue
	 */
	private void updateGame(Game game, String senderEmail, int newValue) {
		game.setLastPlayedBy(senderEmail);
		game.setCurrentValue(newValue);
		gameRepo.saveGame(game);
	}
}
