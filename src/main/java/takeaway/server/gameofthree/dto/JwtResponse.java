package takeaway.server.gameofthree.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtResponse implements Serializable {
	private final String jwttoken;

}