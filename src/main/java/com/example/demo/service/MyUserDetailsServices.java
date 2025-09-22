package com.example.demo.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ProfileEntity;
import com.example.demo.repository.ProfileRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MyUserDetailsServices implements UserDetailsService{
	
	private final ProfileRepo profileRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	ProfileEntity existingUser =	profileRepo.findByEmail(email)
		.orElseThrow(() -> new UsernameNotFoundException("Profile not found with email:"+email));
		return User.builder()
				.username(existingUser.getEmail())
				.password(existingUser.getPassword())
				.authorities(Collections.emptyList())
				.build();
	}

}
