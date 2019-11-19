package takeaway.server.gameofthree.dao;

import java.util.List;

import takeaway.server.gameofthree.dto.Player;

public interface PlayerRepo {

	Player findPlayerInRegistery(String playerEmail);

	Player savePlayerAsAvailable(Player player);

	Player savePlayerAsUnavailable(Player player);

	boolean addPlayerToRegistery(Player player);

	boolean removePlayerFromRegistery(String playerEmail);

	boolean isPlayerAvaliable(String playerEmail);

	List<Player> getAvaliablePlayers();

}
