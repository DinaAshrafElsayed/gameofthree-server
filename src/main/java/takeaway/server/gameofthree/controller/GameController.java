package takeaway.server.gameofthree.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * @author El-sayedD
 *
 */
@RestController
@Validated
public class GameController {
	
	@PostMapping(value = "/takeaway/v1/startGame", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> startGame(@RequestParam(name = "uuid")@NotBlank String uuid,@RequestParam(name = "email")@NotBlank String player2 ) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/takeaway/v1/availablePlayers")
	public ResponseEntity<?> getAvailablePlayers(@RequestParam(name = "uuid")@NotBlank String uuid) {
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PatchMapping(value = "/takeaway/v1/play", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> play(@RequestParam(name = "uuid")@NotBlank String uuid,@RequestParam(name = "value")@NotBlank String value) {
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
