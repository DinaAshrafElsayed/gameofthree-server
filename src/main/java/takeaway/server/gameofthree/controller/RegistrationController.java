package takeaway.server.gameofthree.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import takeaway.server.gameofthree.dto.JwtResponse;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.service.RegistrationService;

/**
 * 
 * @author El-sayedD
 * Responsible for registeration and unregesteration of players
 *
 */
@RestController
@Validated
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	/**
	 * An endpoint for the players to register their data when entering the game
	 * @param player the player to be registered
	 * @throws BusinessException thrown if player is already registered
	 */
	@PostMapping(value = "/takeaway/v1/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@Valid @RequestBody Player player) throws BusinessException {
		String token = registrationService.register(player);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	/**
	 * An endpoint for the players to unregister their data when leaving the game
	 * @throws BusinessException thrown when a token is invalid or user doesn't exist
	 */
	@DeleteMapping(value = "/takeaway/v1/unregister")
	public ResponseEntity<?> unregister() throws BusinessException {
		registrationService.unregister();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
