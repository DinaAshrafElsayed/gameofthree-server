package takeaway.server.gameofthree.dao;

import java.util.List;

import takeaway.server.gameofthree.dto.Player;

/**
 * A player repository for doing the CRUD operations on a player
 * 
 * @author El-sayedD
 */
public interface PlayerRepo {

	Player findPlayerInRegisteryByEmail(String playerEmail);

	Player markPlayerAsAvailable(Player player);

	Player markPlayerAsUnavailable(Player player);

	boolean addPlayerToRegistery(Player player);

	boolean removePlayerFromRegisteryByPlayerEmail(String playerEmail);

	boolean isPlayerAvaliable(String playerEmail);

	List<Player> getAvaliablePlayers();

	boolean updatePlayerByGameId(String playerEmail, String gameId);

	boolean updatePlayerGameIdAndAvailability(String playerEmail, String gameId, boolean isAvailable);

	void updatePlayersList(List<Player> players);

}
