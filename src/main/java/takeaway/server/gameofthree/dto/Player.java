package takeaway.server.gameofthree.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

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

	private static final long serialVersionUID = -7199112174977103043L;
	
	@NotBlank
	@Email
	private String email;
	@NotBlank
	@Pattern(regexp = "^([01]?\\\\d\\\\d?|2[0-4]\\\\d|25[0-5])\\\\.([01]?\\\\d\\\\d?|2[0-4]\\\\d|25[0-5])\\\\.\r\n" + 
			"([01]?\\\\d\\\\d?|2[0-4]\\\\d|25[0-5])\\\\.([01]?\\\\d\\\\d?|2[0-4]\\\\d|25[0-5])$")
	private String ip;
	@NotBlank
	@Pattern(regexp = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")
	private String port;

	private String currentGameId;
	private boolean isAvailable;
}
