package takeaway.server.gameofthree.service;

import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(email);
		return builder.build();
	}

	
}
