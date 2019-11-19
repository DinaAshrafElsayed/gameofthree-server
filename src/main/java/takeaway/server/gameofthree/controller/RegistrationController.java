package takeaway.server.gameofthree.controller;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import takeaway.server.gameofthree.dto.JwtResponse;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.util.JwtTokenUtil;

/**
 * 
 * @author El-sayedD
 *
 */
@RestController
@Validated
public class RegistrationController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping(value = "/takeaway/v1/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@Valid @RequestBody Player player) {
		// TODO make a service layer to generate token and save player in list of
		// players
		String token = jwtTokenUtil.generateToken(player);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@DeleteMapping(value = "/takeaway/v1/unregister")
	public ResponseEntity<?> unregister(@RequestParam(name = "uuid") @NotBlank String uuid) {
		// TODO remove from player lists and make sure that player is not in any game
		// if in any game end it and make other player as winner
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
