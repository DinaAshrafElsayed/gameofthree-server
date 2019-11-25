package takeaway.server.gameofthree.service;

import java.nio.charset.Charset;
import java.util.Random;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author El-sayedD
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	public final String ROLE = "PLAYER";

	/**
	 * retrieves user data using his email
	 * 
	 * @param email user email
	 * @return user data
	 * @throws UsernameNotFoundException if username (email) isn't registered
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String randomPassword = generateRandomPassword();
		UserBuilder builder = User.withUsername(email);
		builder.password(new BCryptPasswordEncoder().encode(randomPassword));
		builder.roles(ROLE);
		return builder.build();
	}

	/**
	 * helper method to generate a random password of 7 characters
	 * 
	 * @return generated password
	 */
	private String generateRandomPassword() {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		return new String(array, Charset.forName("UTF-8"));

	}
}
