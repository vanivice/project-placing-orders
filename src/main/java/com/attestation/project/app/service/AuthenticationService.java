package com.attestation.project.app.service;

import com.attestation.project.app.model.db.entity.Customer;
import com.attestation.project.app.model.dto.request.SignInRequest;
import com.attestation.project.app.model.dto.request.SignUpRequest;
import com.attestation.project.app.model.dto.response.JwtAuthResponse;
import com.attestation.project.app.model.enums.Role;
import com.attestation.project.app.model.enums.UserStat;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerService customerService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String setPasswordEncoder(String password) {
        password = passwordEncoder.encode(password);
        return password;
    }

    // регистрация пользователя
    public JwtAuthResponse signUp(SignUpRequest request) {

        String messageUp = request.getUsername() + " Регистрация прошла успешно";
        Customer user = Customer.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .email(request.getEmail())
                .password(setPasswordEncoder(request.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .status(UserStat.CREATED)
                .build();

        customerService.create(user);
        String jwt = jwtService.generateToken(user);

        return new JwtAuthResponse(jwt, messageUp);
    }

    // авторизация пользователя
    public JwtAuthResponse signIn(SignInRequest request) {

        String messageIn = request.getUsername() + " Авторизация прошла успешно";
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var customer = customerService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(customer);
        return new JwtAuthResponse(jwt, messageIn);
    }
}

