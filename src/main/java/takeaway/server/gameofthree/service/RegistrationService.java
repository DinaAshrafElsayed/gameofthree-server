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
		if (playerRepo.findPlayerInRegistery(player.getEmail()) == null) {
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
		Player player = playerRepo.findPlayerInRegistery(email);
		if (player != null && player.getCurrentGameId() != null && !StringUtils.isEmpty(player.getCurrentGameId())) {
			Game game = gameRepo.findGamePlayedBy(player);
			if (game != null) {
				// TODO
				// end Game and announce other player as winner
				gameRepo.RemoveGame(game.getGameId());
			}
			playerRepo.removePlayerFromRegistery(email);
			playerRepo.savePlayerAsUnavailable(player);
		} else {
			throw new UserDoesnotExistExecption();
		}
	}
}
