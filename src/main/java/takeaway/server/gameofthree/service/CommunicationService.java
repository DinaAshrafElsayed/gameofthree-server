package takeaway.server.gameofthree.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import takeaway.server.gameofthree.dto.GameInvitationStatusEnum;
import takeaway.server.gameofthree.dto.Player;

@Service
public class CommunicationService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${client.acceptGameInvitationPathValue}")
	private String acceptGameInvitationPathValue;
	@Value("${client.playPathValue}")
	private String playPathValue;

	public GameInvitationStatusEnum sendGameInvitation(String senderUsername, Player reciever) {

		String URI = buildURI(reciever, acceptGameInvitationPathValue);

		try {
			// FIXME return correct client response and make sure it's get method
			ResponseEntity<Boolean> response = restTemplate.getForEntity(URI, Boolean.class);
			if (HttpStatus.OK.equals(response.getStatusCode()))
				return response.getBody() ? GameInvitationStatusEnum.ACCEPTED : GameInvitationStatusEnum.DECLINED;
		} catch (RestClientException e) {
			// TODO handle communicationExecption
		}
		return GameInvitationStatusEnum.DECLINED;
	}

	public boolean sendNewValue(String senderUsername, Player reciever, int value) {

		String URI = buildURI(reciever, playPathValue);

		try {
			// FIXME return correct client response and check method
			ResponseEntity<Boolean> response = restTemplate.postForEntity(URI, value,Boolean.class);
			if (HttpStatus.OK.equals(response.getStatusCode()))
				return true;
		} catch (RestClientException e) {
			// TODO handle communicationExecption
		}
		return false;
	}

	private String buildURI(Player reciever, String path) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String URI = builder.host(reciever.getIp()).port(reciever.getPort()).path(path).toUriString();
		return URI;
	}

}
