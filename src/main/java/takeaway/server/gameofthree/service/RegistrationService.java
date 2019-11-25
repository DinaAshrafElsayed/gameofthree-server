package takeaway.server.gameofthree.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import takeaway.server.gameofthree.dao.GameRepo;
import takeaway.server.gameofthree.dao.PlayerRepo;
import takeaway.server.gameofthree.dto.Game;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.exception.UserAlreadyRegisteredException;
import takeaway.server.gameofthree.exception.UserDoesnotExistException;
import takeaway.server.gameofthree.util.JwtTokenUtil;

/**
 * 
 * @author El-sayedD
 *
 */
@Service
public class RegistrationService {

	@Autowired
	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;

	@Autowired
	@Qualifier("GameRepoDefaultImpl")
	private GameRepo gameRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/**
	 * Used to register a player in the server
	 * 
	 * @return token with user data
	 * @throws BusinessException if user is already registered
	 */
	public String register(Player player) throws BusinessException {
		String token = null;
		if (playerRepo.findPlayerInRegisteryByEmail(player.getEmail()) == null) {
			token = jwtTokenUtil.generateToken(player);
			player.setAvailable(true);
			playerRepo.addPlayerToRegistery(player);
		} else {
			throw new UserAlreadyRegisteredException();
		}
		return token;
	}

	/**
	 * Unregister the player from the server
	 * 
	 * @throws BusinessException if token is invalid
	 */
	public void unregister() throws BusinessException {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = user.getUsername();
		Player player = playerRepo.findPlayerInRegisteryByEmail(email);
		if (player != null) {
			String gameId = player.getCurrentGameId();
			if (gameId != null && !StringUtils.isEmpty(gameId)) {
				Game game = gameRepo.findGameById(gameId);
				if (game != null) {
					/*
					 * TODO end Game and announce other player as winner
					 */
					gameRepo.RemoveGameByGameId(gameId);
				}
			}
			playerRepo.markPlayerAsUnavailable(player);
			playerRepo.removePlayerFromRegisteryByPlayerEmail(email);
		} else {
			throw new UserDoesnotExistException();
		}
	}
}
