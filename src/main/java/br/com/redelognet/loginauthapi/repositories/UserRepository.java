package br.com.redelognet.loginauthapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.redelognet.loginauthapi.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);
	
}
