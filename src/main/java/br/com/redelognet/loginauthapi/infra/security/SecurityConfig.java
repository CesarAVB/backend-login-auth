package br.com.redelognet.loginauthapi.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@SuppressWarnings("unused")
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	SecurityFilter securityFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Define um bean que configura a cadeia de filtros de segurança da aplicação
	     http
	            .csrf(csrf -> csrf.disable()) 																	// Desativa a proteção CSRF (Cross-Site Request Forgery), pois a aplicação é stateless
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 	// Configura a gestão de sessão como "stateless", indicando que a aplicação não mantém estado de sessão
	            .authorizeHttpRequests(authorize -> authorize
	                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() 							// Permite acesso público ao endpoint de login
	                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
	                    .anyRequest().authenticated()) 															// Exige autenticação para qualquer outra requisição
	            .cors(cors -> {}) 																				// Habilita o suporte a CORS (Cross-Origin Resource Sharing)
	            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
	            return http.build();																			// Constrói e retorna o objeto SecurityFilterChain configurado
	}	
	
	@Bean
	public PasswordEncoder passwordEncoder() { 														// Define um bean de codificador de senhas para ser usado no sistema
	    return new BCryptPasswordEncoder(); 														// Retorna uma instância de BCryptPasswordEncoder, que é usada para criptografar senhas de maneira segura
	}

	@Bean
	public AuthenticationManager authenticationManagerTeste(AuthenticationConfiguration authenticationConfiguration) throws Exception { 		// Define um bean para o gerenciador de autenticação
	    return authenticationConfiguration.getAuthenticationManager(); 																			// Retorna o gerenciador de autenticação configurado a partir do AuthenticationConfiguration
	}

}
