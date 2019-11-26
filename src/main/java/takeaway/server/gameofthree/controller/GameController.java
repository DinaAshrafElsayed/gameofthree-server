package takeaway.server.gameofthree.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import takeaway.server.gameofthree.dto.GameInvitationResponse;
import takeaway.server.gameofthree.dto.GameInvitationStatusEnum;
import takeaway.server.gameofthree.dto.PlayRequestAndResponse;
import takeaway.server.gameofthree.exception.BusinessException;
import takeaway.server.gameofthree.service.AvailablePlayersService;
import takeaway.server.gameofthree.service.GameInvitationService;
import takeaway.server.gameofthree.service.GameOfThreeService;

/**
 * 
 * @author El-sayedD Responsible for the game invitation, starting and playing
 *
 */
@RestController
@Validated
public class GameController {

	@Autowired
	private GameInvitationService gameInvitationService;

	@Autowired
	private GameOfThreeService gameOfThreeService;

	@Autowired
	private AvailablePlayersService availablePlayersService;

	/**
	 * An endpoint to start a new game with another player
	 * 
	 * @param email       the player to be invited to a new game
	 * @param initalValue the initial value of the game
	 * @throws BusinessException thrown when the other player is not available to
	 *                           start a new game
	 */
	@GetMapping(value = "/takeaway/v1/invite/{email}/play", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> startGame(@PathVariable(name = "email") @NotBlank String email) throws BusinessException {
		GameInvitationStatusEnum status = gameInvitationService.startGame(email);
		return ResponseEntity.ok(new GameInvitationResponse(status.name()));
	}

	/**
	 * An endpoint to retrieve a list of players available to start a new game
	 */
	@GetMapping(value = "/takeaway/v1/availablePlayers")
	public ResponseEntity<?> getAvailablePlayers() {
		return ResponseEntity.ok(availablePlayersService.getAvaliablePlayer());
	}

	/**
	 * An endpoint to be used during the game to submit a new value (playing
	 * player's turn)
	 * 
	 * @param playRequest the request contains the new value to be played
	 * @throws BusinessException
	 */
	@PostMapping(value = "/takeaway/v1/play", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> play(@RequestBody PlayRequestAndResponse playRequest) throws BusinessException {
		PlayRequestAndResponse response = gameOfThreeService.play(playRequest.getValue());
		return ResponseEntity.ok(response);
	}

}
