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
	 * @param value
	 *            new value of game
	 * @throw NoGameExistsException in case that user has no ongoing game
	 * 
	 * @throws NotUserTurnException
	 *             in case that user is sending request and it's not his turn yet
	 * @throws RulesNotAppliedException
	 *             in case rule not matched make sure game is still in progress no
	 *             winner announced yet( value !=1) validate input from previous one
	 *             (matches rule (-1,0,1)) and divisible by 3
	 * 
	 */
	public void play(int value) throws BusinessException {
		String senderEmail = gameOfThreeUtil.extractSenderEmailFromSecurityContext();
		Player sender = gameOfThreeUtil.retrievePlayerIfRegisteredAndAvailable(senderEmail);
		checkIfPlayerHasAnOngoingGame(sender);
		Game game = gameRepo.findGameById(sender.getCurrentGameId());
		checkIfItIsPlayerTurn(game, senderEmail);
		applyRules(game, value);
		value /= 3;
		boolean playerOneWon = value ==1? true: false;
		/*
		 * TODO handle win case else normal case
		 */
		String playerTwoEmail = getPlayerTwoEmail(senderEmail, game);
		Player playerTwo = playerRepo.findPlayerInRegisteryByEmail(playerTwoEmail);
		if (playerTwo != null) {
			communicationService.sendNewValue(senderEmail, playerTwo, value);
		} else {
			/* TODO return playerTwo withdraw return you won */
		}
		/* TODO if game ended I should remove game and update players */
		updateGame(game, senderEmail, value);
	}

	

	public void checkIfPlayerHasAnOngoingGame(Player player) throws BusinessException {
		if (player.getCurrentGameId() == null || player.getCurrentGameId().equals("")) {
			throw new NoGameExistsException();
		}
	}

	public void checkIfItIsPlayerTurn(Game game, String senderEmail) throws BusinessException {
		String lastPlayedBy = game.getLastPlayedBy();
		if (senderEmail.equals(lastPlayedBy)) {
			throw new NotUserTurnException();
		}
	}

	public void applyRules(Game game, int newValue) throws BusinessException {
		int previousValue = game.getCurrentValue();
		if (previousValue - newValue > 1 || previousValue - newValue < -1 && newValue % 3 != 0) {
			throw new RulesViolatedException();
		}
	}

	public String getPlayerTwoEmail(String senderEmail, Game game) {
		String playerTwo;
		if (senderEmail.equals(game.getPlayerOneEmail())) {
			playerTwo = game.getPlayerTwoEmail();
		} else {
			playerTwo = game.getPlayerOneEmail();
		}
		return playerTwo;
	}

	public void updateGame(Game game, String senderEmail, int newValue) {
		game.setLastPlayedBy(senderEmail);
		game.setCurrentValue(newValue);
		gameRepo.saveGame(game);
	}
}
