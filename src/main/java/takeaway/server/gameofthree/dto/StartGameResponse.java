package takeaway.server.gameofthree.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StartGameResponse implements Serializable {

	private static final long serialVersionUID = -7567964842837364845L;
	private String gameInvitationResponse;

	public StartGameResponse(String gameInvitationResponse) {
		this.gameInvitationResponse = gameInvitationResponse;
	}

}
