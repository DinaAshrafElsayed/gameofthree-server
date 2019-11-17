package takeaway.server.gameofthree.controller;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import takeaway.server.gameofthree.dto.JwtResponse;
import takeaway.server.gameofthree.dto.Player;
import takeaway.server.gameofthree.service.UserDetailsServiceImpl;
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
	@Autowired
	private UserDetailsServiceImpl userDetailsService;


	@PostMapping(value = "/takeaway/v1/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@Valid @RequestBody Player player) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(player.getEmail());
		String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@DeleteMapping(value = "/takeaway/v1/unregister")
	public ResponseEntity<?> unregister(@RequestParam(name = "uuid") @NotBlank String uuid) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
