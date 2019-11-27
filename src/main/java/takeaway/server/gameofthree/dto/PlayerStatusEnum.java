package takeaway.server.gameofthree.dto;

import lombok.Getter;

public enum PlayerStatusEnum {
	WON("you won"), LOST("you lost");

	@Getter
	String message;

	private PlayerStatusEnum(String message) {
		this.message = message;
	}
}
