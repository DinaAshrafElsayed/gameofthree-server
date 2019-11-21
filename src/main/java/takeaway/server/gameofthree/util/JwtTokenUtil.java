package takeaway.server.gameofthree.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import takeaway.server.gameofthree.dto.Player;

/**
 * 
 * Helper method for generation and validation of JWT
 * 
 * @author El-sayedD
 */
@Component
public class JwtTokenUtil implements Serializable {
	private static final long serialVersionUID = -2550185165626007488L;
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	@Value("${jwt.secret}")
	private String secret;

	/**
	 * retrieve username from jwt token
	 * 
	 * @param token the token from which username is extracted
	 * @return the username
	 */
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * retrieve expiration date from token
	 * 
	 * @param token the token from which expiration date is extracted
	 * @return expiration date
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	/**
	 * Extracts data from token
	 * 
	 * @param token          the token from which data are extracted
	 * @param claimsResolver a wrapper for the key of data to be extracted
	 * @return the extracted data
	 */
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Retrieves all information from token
	 * 
	 * @param token the token from which data are extracted
	 * 
	 * @return information extracted
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * check if the token has expired
	 * 
	 * @param token the token to be checked
	 * @return true if expired, false otherwise
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	/**
	 * generate token for user
	 * 
	 * @param player the user for which a token is generated
	 * @return the generated token
	 */
	public String generateToken(Player player) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, player.getEmail());
	}

	/**
	 * Generate token from certain claims
	 * while creating the token -
	 * 	 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	 * 	 2. Sign the JWT using the HS512 algorithm and secret key.
	 * 	 3. According to JWS Compact
	 * 	 Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	 * 	 compaction of the JWT to a URL-safe string
	 * @return token generated
	 */
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * Check if token is valid against certain username
	 * 
	 * @param token       token to be checked
	 * @param userDetails carrying the username
	 * @return true if valid non expired token, false otherwise
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}