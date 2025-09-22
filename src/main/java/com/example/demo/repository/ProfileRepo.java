package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProfileEntity;

public interface ProfileRepo extends JpaRepository<ProfileEntity,Long> {

	Optional<ProfileEntity> findByEmail(String email);
	Optional<ProfileEntity> findByActivationToken(String activationToken);
}
