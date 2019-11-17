package takeaway.server.gameofthree.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
/**
 * 
 * @author El-sayedD
 *
 */
@Data
public class Player {
	@NotBlank
	String email;
	@NotBlank
	String ip;
	@NotBlank
	String port;
}
