package takeaway.server.gameofthree.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import takeaway.server.gameofthree.dao.GameRepo;
import takeaway.server.gameofthree.dao.PlayerRepo;
import takeaway.server.gameofthree.dto.Game;
import takeaway.server.gameofthree.dto.PlayRequestAndResponse;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.dto.PlayerStatusEnum;
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
	public PlayRequestAndResponse play(int value) throws BusinessException {
		String senderEmail = gameOfThreeUtil.extractSenderEmailFromSecurityContext();
		Player sender = playerRepo.findPlayerInRegisteryByEmail(senderEmail);
		checkIfPlayerHasAnOngoingGame(sender);
		Game game = gameRepo.findGameById(sender.getCurrentGameId());
		boolean firstRound = false;
		if (game.getLastPlayedBy() != null && !game.getLastPlayedBy().equals("")) {
			checkIfItIsPlayerTurn(game, senderEmail);
			applyRules(game, value);
			value /= 3;
		} else {
			firstRound = true;
		}
		boolean playerTwoWon = false;
		boolean playerOneWon = value == 1 ? true : false;
		String playerTwoEmail = getPlayerTwoEmail(senderEmail, game);
		Player playerTwo = playerRepo.findPlayerInRegisteryByEmail(playerTwoEmail);
		updateGame(game, senderEmail, value);
		PlayRequestAndResponse response = new PlayRequestAndResponse();
		if (playerTwo != null) {
			response = communicationService.sendValueAndRecieveNewValue(playerTwo, value, firstRound,
					playerTwo.getInputChoice(),playerOneWon);
			int newValue = extractDataFromResponse(game, firstRound, playerTwo, response);
			newValue /= 3;
			playerTwoWon = newValue == 1 ? true : false;
			updateGame(game, playerTwoEmail, newValue);
			response.setValue(newValue);
		} else {
			playerOneWon = true;
		}
		response = endGameIfOneOfPlayersWon(playerOneWon, playerTwoWon, senderEmail, playerTwoEmail, game, response);
		return response;
	}

	private PlayRequestAndResponse endGameIfOneOfPlayersWon(boolean playerOneWon, boolean playerTwoWon,
			String senderEmail, String playerTwoEmail, Game game, PlayRequestAndResponse response) {
		if (playerOneWon || playerTwoWon) {
			gameRepo.RemoveGameByGameId(game.getGameId());
			playerRepo.updatePlayerGameIdAndAvailability(senderEmail, null, true);
			playerRepo.updatePlayerGameIdAndAvailability(playerTwoEmail, null, true);
			response.setPlayerStatusEnum(playerOneWon ? PlayerStatusEnum.WON : PlayerStatusEnum.LOST);
		}
		return response;
	}

	private int extractDataFromResponse(Game game, boolean firstRound, Player playerTwo,
			PlayRequestAndResponse response) throws BusinessException {
		int newValue = response.getValue();
		applyRules(game, newValue);
		updatePlayerTwoWithHisInputChoice(firstRound, playerTwo, response);
		return newValue;
	}

	private void updatePlayerTwoWithHisInputChoice(boolean firstRound, Player playerTwo,
			PlayRequestAndResponse response) {
		if (firstRound) {
			playerTwo.setInputChoice(response.getInputChoice());
			List<Player> players = new ArrayList<>(1);
			players.add(playerTwo);
			playerRepo.updatePlayersList(players);
		}
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
