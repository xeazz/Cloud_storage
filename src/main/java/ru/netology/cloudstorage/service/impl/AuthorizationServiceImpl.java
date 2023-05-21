package ru.netology.cloudstorage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.model.Request.AuthorizationRequest;
import ru.netology.cloudstorage.model.Response.AuthorizationResponse;
import ru.netology.cloudstorage.repository.AuthorizationRepository;
import ru.netology.cloudstorage.security.JwtTokenProvider;
import ru.netology.cloudstorage.service.AuthorizationService;
import ru.netology.cloudstorage.service.UserService;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {
    private final AuthenticationManager authenticationManager;
    private final AuthorizationRepository authorizationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public AuthorizationResponse login(AuthorizationRequest request) {
        log.info("Login and password in the UsernamePasswordAuthenticationToken class and pass the " +
                "AuthenticationManager instance for verification");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        final User user = userService.getByUsername(request.getLogin());
        final String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRoles());
        authorizationRepository.addTokenAndUsername(token, user);
        log.info("User: {} successfully authorized", user.getUsername());
        return AuthorizationResponse.builder().token(token).build();
    }
}
