package com.Khata.Khata.Service;

import com.Khata.Khata.Dto.AuthResponse;
import com.Khata.Khata.Dto.LoginRequest;
import com.Khata.Khata.Dto.SignupRequest;
import com.Khata.Khata.Entity.User;
import com.Khata.Khata.Exception.ApiException;
import com.Khata.Khata.Repository.UserRepository;
import com.Khata.Khata.Security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse signup(SignupRequest request)
    {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email))
        {
            throw ApiException.conflict("an account with this email already exists");
        }
        User user = new User(null, request.getUserName().trim(), email,
                passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        return new AuthResponse(jwtUtil.generateToken(saved.getId()), saved);
    }

    public AuthResponse login(LoginRequest request)
    {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> ApiException.unauthorized("invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw ApiException.unauthorized("invalid email or password");
        }
        return new AuthResponse(jwtUtil.generateToken(user.getId()), user);
    }
}
