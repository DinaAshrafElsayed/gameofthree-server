package takeaway.server.gameofthree.dao;

import java.util.List;

import takeaway.server.gameofthree.dto.Player;

public interface PlayerRepo {

	Player findPlayerInRegisteryByEmail(String playerEmail);

	Player savePlayerAsAvailable(Player player);

	Player savePlayerAsUnavailable(Player player);

	boolean addPlayerToRegistery(Player player);

	boolean removePlayerFromRegisteryByPlayerEmail(String playerEmail);

	boolean isPlayerAvaliable(String playerEmail);

	List<Player> getAvaliablePlayers();

}
