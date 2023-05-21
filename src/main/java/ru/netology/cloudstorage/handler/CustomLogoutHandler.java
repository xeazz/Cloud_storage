package ru.netology.cloudstorage.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.exception.ResourceNotFoundException;
import ru.netology.cloudstorage.repository.AuthorizationRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
    private final AuthorizationRepository authorizationRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("auth-token");
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        final String username = authorizationRepository
                .getUsernameByToken(token).orElseThrow(
                        () -> new ResourceNotFoundException("No user found with this token")).getUsername();
        log.info("User {} with token {} successfully deleted", username, token);
        authorizationRepository.removeTokenAndUsernameByToken(token);
    }
}
