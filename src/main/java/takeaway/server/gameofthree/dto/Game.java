package takeaway.server.gameofthree.dto;

import java.util.UUID;

import lombok.Data;

/**
 * Represents a running game holding information about the players playing, the
 * last played value, and by whom it was played
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
		/* generate game uniqueID */
		UUID u = UUID.randomUUID();
		gameId = u.toString();
	}
}
