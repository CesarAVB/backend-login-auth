package br.com.redelognet.loginauthapi.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.redelognet.loginauthapi.dto.LoginRequestDTO;
import br.com.redelognet.loginauthapi.dto.RegisterRequestDTO;
import br.com.redelognet.loginauthapi.dto.ResponseDTO;
import br.com.redelognet.loginauthapi.entities.User;
import br.com.redelognet.loginauthapi.infra.security.TokenService;
import br.com.redelognet.loginauthapi.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private UserRepository repository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token, user.getPerfil()));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterRequestDTO body){
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            newUser.setPerfil(body.perfil());
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token, newUser.getPerfil()));
        }
        return ResponseEntity.badRequest().build();
    }
}