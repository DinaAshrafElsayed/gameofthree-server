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
import takeaway.server.gameofthree.exception.UserDoesnotExistExecption;
import takeaway.server.gameofthree.util.JwtTokenUtil;

/**
 * 
 * @author El-sayedD
 *
 */
@Service
public class RegistrationService {

	@Qualifier("PlayerRepoDefaultImpl")
	private PlayerRepo playerRepo;

	@Qualifier("GameRepoDefaultImpl")
	private GameRepo gameRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public String register(Player player) throws BusinessException {
		String token = null;
		if (playerRepo.findPlayerInRegisteryByEmail(player.getEmail()) == null) {
			token = jwtTokenUtil.generateToken(player);
			playerRepo.addPlayerToRegistery(player);
			playerRepo.savePlayerAsAvailable(player);
		} else {
			throw new UserAlreadyRegisteredException();
		}
		return token;
	}

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
			playerRepo.savePlayerAsUnavailable(player);
			playerRepo.removePlayerFromRegisteryByPlayerEmail(email);
		} else {
			throw new UserDoesnotExistExecption();
		}
	}
}
