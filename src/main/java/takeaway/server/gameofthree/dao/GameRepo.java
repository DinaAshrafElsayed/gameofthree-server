package takeaway.server.gameofthree.dao;

import takeaway.server.gameofthree.dto.Game;
import takeaway.server.gameofthree.dto.Player;

public interface GameRepo {

	boolean saveGame(Game game);

	boolean RemoveGame(String gameId);

	Game findGamePlayedBy(Player player);

}
