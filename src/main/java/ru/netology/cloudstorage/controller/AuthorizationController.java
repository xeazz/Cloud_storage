package ru.netology.cloudstorage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudstorage.model.Request.AuthorizationRequest;
import ru.netology.cloudstorage.model.Response.AuthorizationResponse;
import ru.netology.cloudstorage.service.AuthorizationService;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponse> authorizationRequest(@RequestBody AuthorizationRequest request) {
        return ResponseEntity.ok(authorizationService.login(request));
    }
}
