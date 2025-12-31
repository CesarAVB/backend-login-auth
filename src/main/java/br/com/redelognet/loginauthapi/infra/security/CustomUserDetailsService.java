package br.com.redelognet.loginauthapi.infra.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import br.com.redelognet.loginauthapi.entities.User;
import br.com.redelognet.loginauthapi.repositories.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
    @Autowired
    private UserRepository repository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String roleFromDb = user.getPerfil(); 
        var authorities = Collections.singletonList( new SimpleGrantedAuthority("ROLE_" + roleFromDb.toUpperCase()) );
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
