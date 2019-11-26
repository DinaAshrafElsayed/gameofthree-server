package takeaway.server.gameofthree.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * Represents a value in the game
 * 
 * @author El-sayedD
 *
 */
@Data
public class PlayRequestAndResponse implements Serializable {

	private static final long serialVersionUID = 6860603557450916164L;
	private int value;
	private String inputChoice;

	public PlayRequestAndResponse() {

	}

	public PlayRequestAndResponse(int value) {
		super();
		this.value = value;
	}
}
