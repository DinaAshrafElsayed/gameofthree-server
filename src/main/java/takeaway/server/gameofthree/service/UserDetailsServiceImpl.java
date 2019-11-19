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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	public final String ROLE = "ADMIN";

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String randomPassword = generateRandomPassword();
		UserBuilder builder = User.withUsername(email);
		builder.password(new BCryptPasswordEncoder().encode(randomPassword));
		builder.roles(ROLE);
		return builder.build();
	}

	private String generateRandomPassword() {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		return new String(array, Charset.forName("UTF-8"));

	}
}
