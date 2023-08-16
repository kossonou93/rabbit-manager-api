package com.rabbit.manager.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rabbit.manager.exception.NotFoundException;
import com.rabbit.manager.model.User;
import com.rabbit.manager.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		try {
			user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found for this username :: " + username));
			if (user==null)
				throw new UsernameNotFoundException("Utilisateur introuvable !");
				//logger.error("User Not Found with username: " + username);
				List<GrantedAuthority> auths = new ArrayList<>();
				user.getRoles().forEach(role -> {
					GrantedAuthority auhority = new SimpleGrantedAuthority(role.getName().toString());
					auths.add(auhority);
				});
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Errorr =========> ");
			e.printStackTrace();
		}
			return UserDetailsImpl.build(user);
			//return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),auths);
		}
}