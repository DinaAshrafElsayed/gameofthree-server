package takeaway.server.gameofthree.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import takeaway.server.gameofthree.dto.Game;
import takeaway.server.gameofthree.dto.Player;

@Repository("GameRepoDefaultImpl")
public class GameRepoDefaultImpl implements GameRepo {

	private Map<String, Game> gameMap;

	@PostConstruct
	void init() {
		gameMap = new ConcurrentHashMap<>();
	}

	@Override
	public boolean saveGame(Game game) {
		// TODO Auto-generated method stub
		return gameMap.put(game.getGameId(), game) != null ? true : false;
	}

	@Override
	public boolean RemoveGame(String gameId) {
		return  gameMap.remove(gameId) != null ? true : false;;
	}

	@Override
	public Game findGamePlayedBy(Player player) {
		String gameId = player.getCurrentGameId();
		return gameMap.get(gameId);
	}

}
