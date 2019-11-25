package takeaway.server.gameofthree.dao;

import takeaway.server.gameofthree.dto.Game;
import takeaway.server.gameofthree.dto.Player;

/**
 * A repository to store, retrieve and remove games by ID
 * 
 * @author El-sayedD
 */
public interface GameRepo {

	boolean saveGame(Game game);

	boolean RemoveGameByGameId(String gameId);

	Game findGameById(String gameId);

}
