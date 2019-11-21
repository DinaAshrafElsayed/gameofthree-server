package takeaway.server.gameofthree.dao;

import takeaway.server.gameofthree.dto.Game;

public interface GameRepo {

	boolean saveGame(Game game);

	boolean RemoveGameByGameId(String gameId);

	Game findGameById(String gameId);

}
