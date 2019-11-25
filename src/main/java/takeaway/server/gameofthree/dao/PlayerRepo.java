package takeaway.server.gameofthree.dao;

import java.util.List;
import java.util.Set;

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

	Set<String> getAvailablePlayersEmailNotEqualEmail(String playerEmail);

	boolean updatePlayerByGameId(String playerEmail, String gameId);

	boolean updatePlayerGameIdAndAvailability(String playerEmail, String gameId, boolean isAvailable);

	void updatePlayersList(List<Player> players);

}
