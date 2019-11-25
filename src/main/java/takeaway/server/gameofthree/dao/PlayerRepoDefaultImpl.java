package takeaway.server.gameofthree.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import takeaway.server.gameofthree.dto.Player;

/**
 * Actual implementation of the player repository using Concurrent Maps to be
 * thread safe
 * 
 * @author El-sayedD
 */
@Repository("PlayerRepoDefaultImpl")
@Scope("singleton")
public class PlayerRepoDefaultImpl implements PlayerRepo {

	private static Map<String, Player> registeredPlayersMap;

	@PostConstruct
	void init() {
		registeredPlayersMap = new ConcurrentHashMap<>();
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
		Player player = registeredPlayersMap.get(playerEmail);
		return player.isAvailable();
	}

	@Override
	public Player findPlayerInRegisteryByEmail(String playerEmail) {
		return registeredPlayersMap.get(playerEmail);
	}

	@Override
	public Set<String> getAvailablePlayersEmailNotEqualEmail(String playerEmail) {
		Set<String> availablePlalyers = registeredPlayersMap.values().stream()
				.filter(p -> p.isAvailable() && !p.getEmail().equals(playerEmail)).map(Player::getEmail)
				.collect(Collectors.toSet());
		return availablePlalyers;
	}

	@Override
	public Player markPlayerAsAvailable(Player player) {
		player.setAvailable(true);
		registeredPlayersMap.put(player.getEmail(), player);
		return registeredPlayersMap.get(player.getEmail());
	}

	@Override
	public Player markPlayerAsUnavailable(Player player) {
		player.setAvailable(false);
		registeredPlayersMap.put(player.getEmail(), player);
		return registeredPlayersMap.get(player.getEmail());
	}

	@Override
	public boolean updatePlayerByGameId(String playerEmail, String gameId) {
		if (registeredPlayersMap.containsKey(playerEmail)) {
			Player player = registeredPlayersMap.get(playerEmail);
			player.setCurrentGameId(gameId);
			return registeredPlayersMap.put(playerEmail, player) != null ? true : false;
		}
		return false;
	}

	@Override
	public boolean updatePlayerGameIdAndAvailability(String playerEmail, String gameId, boolean isAvailable) {
		if (registeredPlayersMap.containsKey(playerEmail)) {
			Player player = registeredPlayersMap.get(playerEmail);
			player.setAvailable(isAvailable);
			player.setCurrentGameId(gameId);
			return registeredPlayersMap.put(playerEmail, player) != null ? true : false;
		}
		return false;
	}

	@Override
	public void updatePlayersList(List<Player> players) {
		for (Player player : players) {
			registeredPlayersMap.put(player.getEmail(), player);
		}
	}

}
