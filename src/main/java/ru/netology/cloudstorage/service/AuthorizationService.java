package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.model.Request.AuthorizationRequest;
import ru.netology.cloudstorage.model.Response.AuthorizationResponse;

public interface AuthorizationService {
    AuthorizationResponse login(AuthorizationRequest request);

}
