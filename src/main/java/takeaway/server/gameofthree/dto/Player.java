package takeaway.server.gameofthree.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * 
 * @author El-sayedD
 *
 */
@Data
public class Player implements Serializable {

	public Player() {
	}

	public Player(String email) {
		super();
		this.email = email;
	}

	@NotBlank
	@Email
	String email;
	@NotBlank
	String ip;
	@NotBlank
	String port;
}
