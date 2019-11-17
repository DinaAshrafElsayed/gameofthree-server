package takeaway.server.gameofthree.controller;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import takeaway.server.gameofthree.dto.Player;
/**
 * 
 * @author El-sayedD
 *
 */
@RestController
@Validated
public class RegistrationController {

	@PostMapping(value = "/takeaway/v1/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@Valid @RequestBody Player player) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping(value = "/takeaway/v1/unregister")
	public ResponseEntity<?> unregister(@RequestParam(name = "uuid")@NotBlank String uuid) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
