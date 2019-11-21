package takeaway.server.gameofthree.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import takeaway.server.gameofthree.dto.GameInvitationStatusEnum;
import takeaway.server.gameofthree.dto.StartGameResponse;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.service.GameOfThreeService;

/**
 * 
 * @author El-sayedD
 *
 */
@RestController
@Validated
public class GameController {

	@Autowired
	private GameOfThreeService gameOfThreeService;

	@PostMapping(value = "/takeaway/v1/invite/{email}/play", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> startGame(@PathVariable(name = "email") @NotBlank String email,
			@RequestParam(name = "initalValue", required = true) int initalValue )  throws BusinessException {
		GameInvitationStatusEnum status = gameOfThreeService.startGame(email, initalValue);
		return ResponseEntity.ok(new StartGameResponse(status.name()));
	}

	@GetMapping(value = "/takeaway/v1/availablePlayers")
	public ResponseEntity<?> getAvailablePlayers() {
		return ResponseEntity.ok(gameOfThreeService.getAvaliablePlayer());
	}

	@PatchMapping(value = "/takeaway/v1/play", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> play(@RequestParam(name = "value") @NotBlank String value) {
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
