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

	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String ip;
	@NotBlank
	private String port;

	private String currentGameId;
	private boolean isAvailable;
}
