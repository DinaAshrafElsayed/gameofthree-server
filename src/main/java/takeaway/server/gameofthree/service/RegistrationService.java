package takeaway.server.gameofthree.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.util.JwtTokenUtil;

/**
 * 
 * @author El-sayedD
 *
 */
@Service
public class RegistrationService {

	Map<String, Player> registeredPlayersMap;
	Map<String, Player> AvaliablePlayersMap;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public String register(Player player) {
		String token = null;
		if (!registeredPlayersMap.containsKey(player.getEmail())) {
			token = jwtTokenUtil.generateToken(player);
			registeredPlayersMap.put(player.getEmail(), player);
			AvaliablePlayersMap.put(player.getEmail(), player);
		} else {
			// TODO throw bad request if player is already registered
		}
		return token;
	}

	public void unregister() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = user.getUsername();
		if (registeredPlayersMap.containsKey(email)) {
			registeredPlayersMap.remove(email);
			// TODO list
			// remove from map of available players if available else
			// remove from game
			// make sure that player is not in any game (game map)
			// if in any game end it and make other player as winner
		} else {
			// TODO throw bad request if player is already registered
		}
	}

	@PostConstruct
	public void initaleMaps() {
		registeredPlayersMap = new ConcurrentHashMap<>();
		AvaliablePlayersMap = new ConcurrentHashMap<>();
	}

}
