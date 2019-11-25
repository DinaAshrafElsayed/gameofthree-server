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
public class PlayRequest implements Serializable{
	
	private static final long serialVersionUID = 6860603557450916164L;
	private int value;
	public PlayRequest() {
		
	}
	public PlayRequest(int value) {
		super();
		this.value = value;
	}
}
