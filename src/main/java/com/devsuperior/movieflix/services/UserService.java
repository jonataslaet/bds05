package com.devsuperior.movieflix.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devsuperior.movieflix.controllers.dtos.UserDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;

	public UserDTO findSelfProfile() {
		User authenticatedUser = authService.getAuthenticatedUser();
		return new UserDTO(authenticatedUser);
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userByEmail = userRepository.findByEmail(email);
		if (!userByEmail.isPresent()) {
			throw new EntityNotFoundException("User not found for email: " + email);
		}
		return userByEmail.get();
	}

}
