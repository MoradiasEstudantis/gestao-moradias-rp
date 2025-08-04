package com.moradiasestudantis.gestao_moradias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moradiasestudantis.gestao_moradias.dto.RegisterDto;
import com.moradiasestudantis.gestao_moradias.exception.EmailAlreadyExistsException;
import com.moradiasestudantis.gestao_moradias.model.User;
import com.moradiasestudantis.gestao_moradias.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Para autenticação do Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    // Para registrar novo usuário
    public void register(RegisterDto data) {
        if (userRepository.existsByEmail(data.getEmail())) {
            throw new EmailAlreadyExistsException("Este e-mail já está cadastrado.");
        }

        User newUser = new User();
        newUser.setEmail(data.getEmail());
        newUser.setSenha(passwordEncoder.encode(data.getSenha()));
        newUser.setRole(data.getRole());

        userRepository.save(newUser);
    }
}
