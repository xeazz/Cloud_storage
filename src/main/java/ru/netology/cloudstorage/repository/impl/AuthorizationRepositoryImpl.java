package ru.netology.cloudstorage.repository.impl;

import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Repository;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.repository.AuthorizationRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@EqualsAndHashCode
public class AuthorizationRepositoryImpl implements AuthorizationRepository {
    private final Map<String, User> personMap = new ConcurrentHashMap<>();

    @Override
    public void addTokenAndUsername(String token, User user) {
        personMap.putIfAbsent(token, user);
    }

    public Optional<User> getUsernameByToken(String token) {
        return Optional.ofNullable(personMap.get(token));
    }

    public void removeTokenAndUsernameByToken(String token) {
        personMap.remove(token);
    }

    public Map<String, User> getPersonMap() {
        return personMap;
    }

}
