package com.omnistore.services;

import com.omnistore.entity.Session;
import com.omnistore.entity.User;
import com.omnistore.repository.SessionRepository;
import com.omnistore.repository.UserRepository;
import com.omnistore.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user.getEmail(), user.getRole().name());

        // Create and save session
        Session session = new Session(user, token, LocalDateTime.now().plusHours(24));
        sessionRepository.save(session);

        return token;
    }

    @Transactional
    public void logout(String token) {
        sessionRepository.deleteByToken(token);
    }
}
