package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.entity.User;

public interface UserService {
    User getByUsername(String username);
}
