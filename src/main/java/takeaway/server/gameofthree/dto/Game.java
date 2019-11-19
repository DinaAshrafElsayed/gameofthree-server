package takeaway.server.gameofthree.dto;

import lombok.Data;
/**
 * 
 * @author El-sayedD
 *
 */
@Data
public class Game {
	private String playerOneEmail;
	private String playerTwoEmail;
	private int CurrentValue;
	private String lastPlayedBy;
	private String gameId;
	public Game() {
		//generate game uniqueID
	}
}
