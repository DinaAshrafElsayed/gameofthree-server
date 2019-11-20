package takeaway.server.gameofthree.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import takeaway.server.gameofthree.dto.Player;

@Repository("PlayerRepoDefaultImpl")
public class PlayerRepoDefaultImpl implements PlayerRepo {

	private Map<String, Player> registeredPlayersMap;
	private Map<String, Player> availablePlayersMap;

	@PostConstruct
	void init() {
		registeredPlayersMap = new ConcurrentHashMap<>();
		availablePlayersMap = new ConcurrentHashMap<>();
	}

	@Override
	public boolean addPlayerToRegistery(Player player) {
		return registeredPlayersMap.put(player.getEmail(), player) != null ? true : false;
	}

	@Override
	public boolean removePlayerFromRegisteryByPlayerEmail(String playerEmail) {
		return registeredPlayersMap.remove(playerEmail) != null ? true : false;
	}

	@Override
	public boolean isPlayerAvaliable(String playerEmail) {
		return availablePlayersMap.containsKey(playerEmail);
	}

	@Override
	public Player findPlayerInRegisteryByEmail(String playerEmail) {
		return registeredPlayersMap.get(playerEmail);
	}

	@Override
	public List<Player> getAvaliablePlayers() {
		return new ArrayList<>(availablePlayersMap.values());
	}

	@Override
	public Player savePlayerAsAvailable(Player player) {
		return availablePlayersMap.put(player.getEmail(), player);
	}

	@Override
	public Player savePlayerAsUnavailable(Player player) {
		return availablePlayersMap.remove(player.getEmail());
	}

}
