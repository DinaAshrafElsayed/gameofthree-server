package takeaway.server.gameofthree.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public String register(Player player) {
		String token = jwtTokenUtil.generateToken(player);
		Map<String, Player> map = new ConcurrentHashMap<>();
		// make a service layer to generate token and save player in list of
		// players
		// throw bad request if player is already registered
		// add to map email players with String email as key and player as object
		// map of available players with String email as key and player as object
		return token;
	}

	public void unregister() {
		// TODO list
		// get player from token
		// throw bad request if player is doesn't exist
		// remove to map email players with String email as key and player as object
		// remove from map of available players with String email as key and player as
		// object
		// make sure that player is not in any game (game map)
		// if in any game end it and make other player as winner
	}

}
