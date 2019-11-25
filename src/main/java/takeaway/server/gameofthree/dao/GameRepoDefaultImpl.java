package takeaway.server.gameofthree.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import takeaway.server.gameofthree.dto.Game;

/**
 * Actual implementation of the game repository using Concurrent Maps to be
 * thread safe
 * 
 * @author El-sayedD
 */
@Repository("GameRepoDefaultImpl")
public class GameRepoDefaultImpl implements GameRepo {

	private Map<String, Game> gameMap;

	@PostConstruct
	void init() {
		gameMap = new ConcurrentHashMap<>();
	}

	@Override
	public boolean saveGame(Game game) {
		gameMap.put(game.getGameId(), game);
		return gameMap.containsKey(game.getGameId());
	}

	@Override
	public boolean RemoveGameByGameId(String gameId) {
		return gameMap.remove(gameId) != null ? true : false;
	}

	@Override
	public Game findGameById(String gameId) {
		return gameMap.get(gameId);
	}

}
