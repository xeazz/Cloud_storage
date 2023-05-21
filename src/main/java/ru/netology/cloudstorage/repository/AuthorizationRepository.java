package ru.netology.cloudstorage.repository;

import ru.netology.cloudstorage.entity.User;

import java.util.Map;
import java.util.Optional;

public interface AuthorizationRepository {

    void addTokenAndUsername(String token, User user);
    Optional<User> getUsernameByToken (String token);
    void removeTokenAndUsernameByToken (String token);
    Map<String, User> getPersonMap ();
}
