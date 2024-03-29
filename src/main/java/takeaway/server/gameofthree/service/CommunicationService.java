package takeaway.server.gameofthree.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import takeaway.server.gameofthree.dto.GameInvitationResponse;
import takeaway.server.gameofthree.dto.GameInvitationStatusEnum;
import takeaway.server.gameofthree.dto.PlayRequestAndResponse;
import takeaway.server.gameofthree.dto.Player;

/**
 * The service responsible for communication between players (through the
 * server)
 * 
 * @author El-sayedD
 */
@Slf4j
@Service
public class CommunicationService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${client.acceptGameInvitationPathValue}")
	private String acceptGameInvitationPathValue;
	@Value("${client.playPathValue}")
	private String playPathValue;
	@Value("${client.accept.invite.emailQueryParam}")
	private String email;
	private final String HTTP = "http";

	/**
	 * Used by the inviting player to send an invitation request to start a new game
	 * with the receiver player
	 * 
	 * @param senderUsername the username of the inviting player
	 * @param reciever       the player to be invited
	 * @return response to the invitation either ACCEPTED or DECLINED
	 */
	public GameInvitationStatusEnum sendGameInvitation(String senderUsername, Player reciever) {

		String URI = buildURI(reciever, acceptGameInvitationPathValue);

		try {
			URI = addQueryParam(URI, email, reciever.getEmail());
			ResponseEntity<GameInvitationResponse> response = restTemplate.getForEntity(URI,
					GameInvitationResponse.class);
			if (HttpStatus.OK.equals(response.getStatusCode()))
				return GameInvitationStatusEnum.ACCEPTED.name().equals(response.getBody().getAcceptInvitationResponse())
						? GameInvitationStatusEnum.ACCEPTED
						: GameInvitationStatusEnum.DECLINED;
		} catch (RestClientException e) {
			log.error(e.getMessage());
		}
		return GameInvitationStatusEnum.DECLINED;
	}

	/**
	 * Used by a player to send a new value (play their turn) during the game
	 * 
	 * @param reciever    receiver player of the new value
	 * @param value       the value to be sent
	 * @param firstRounds a boolean represent if it is the first round or not
	 * @param inputchoice it is the choice of player to play (manual/automatic)
	 * @param playerOnWon a boolean that holds value if eitherPlayer one won or not
	 * @return true if value reached the other player, false otherwise
	 */
	public PlayRequestAndResponse sendValueAndRecieveNewValue(Player reciever, int value, boolean firstRound,
			String inputChoice, boolean playerOneWon) {
		String URI = buildURI(reciever, playPathValue);
		ResponseEntity<PlayRequestAndResponse> response = null;
		try {
			URI = addQueryParam(URI, "value", String.valueOf(value));
			URI = addQueryParam(URI, "firstRound", String.valueOf(firstRound));
			URI = addQueryParam(URI, "inputChoice", inputChoice);
			URI = addQueryParam(URI, "playerOneWon", String.valueOf(playerOneWon));
			response = restTemplate.getForEntity(URI, PlayRequestAndResponse.class);
		} catch (RestClientException e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error(e.getMessage());
		}
		return response.getBody();
	}

	private String buildURI(Player reciever, String path) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String URI = builder.scheme(HTTP).host(reciever.getIp()).port(reciever.getPort()).path(path).toUriString();
		return URI;
	}

	private String addQueryParam(String uri, String QueryParamName, String QueryParamValue) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
		String URI = builder.queryParam(QueryParamName, QueryParamValue).toUriString();
		return URI;
	}
}
