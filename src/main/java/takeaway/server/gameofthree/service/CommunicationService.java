package takeaway.server.gameofthree.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import takeaway.server.gameofthree.dto.Player;

@Service
public class CommunicationService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${client.acceptGameInvitationPathValue}")
	private String pathSql;

	public boolean AcceptGameInvitation(String senderUsername, Player reciever) {

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String URI = builder.host(reciever.getIp()).port(reciever.getPort()).path(pathSql).toUriString();

		//ResponseEntity<Boolean> response = restTemplate.getForEntity(URI, Boolean.class);
		// if (HttpStatus.OK.equals(response.getStatusCode()))
		// return response.getBody();
		return true;
		/*
		 * else return false;
		 */
	}

}
