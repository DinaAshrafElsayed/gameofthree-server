package takeaway.server.gameofthree.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.util.JwtTokenUtil;

/**
 * 
 * @author El-sayedD
 *
 */
@Service
public class RegistrationService {

	Map<String, Player> registeredPlayersMap;
	Map<String, Player> avaliablePlayersMap;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public String register(Player player) throws BusinessException {
		String token = null;
		if (!registeredPlayersMap.containsKey(player.getEmail())) {
			token = jwtTokenUtil.generateToken(player);
			registeredPlayersMap.put(player.getEmail(), player);
			avaliablePlayersMap.put(player.getEmail(), player);
		} else {
			throw new BusinessException("user already registered", HttpStatus.BAD_REQUEST);
		}
		return token;
	}

	public void unregister() throws BusinessException {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = user.getUsername();
		if (registeredPlayersMap.containsKey(email)) {
			registeredPlayersMap.remove(email);
			if(avaliablePlayersMap.containsKey(email)) {
				avaliablePlayersMap.remove(email);
			}else {
				//TODO
				// remove from game
				// make sure that player is not in any game (game map)
				// if in any game end it and make other player as winner
			}
			
		} else {
			throw new BusinessException("user doesn't exist", HttpStatus.BAD_REQUEST);
		}
	}

	@PostConstruct
	public void initaleMaps() {
		registeredPlayersMap = new ConcurrentHashMap<>();
		avaliablePlayersMap = new ConcurrentHashMap<>();
	}

}
