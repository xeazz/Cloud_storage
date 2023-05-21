package ru.netology.cloudstorage.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.repository.UserRepository;
import ru.netology.cloudstorage.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserServiceTest {
    static final String USERNAME = "admin";
    static final String USERNAME_2 = "user";
    static final String PASSWORD = "123qwe321";
    static final Long ID = 12L;
    UserService userService;
    @Mock
    UserRepository userRepository;
    User user;


    @BeforeAll
    public static void initTransferRepository() {
        log.info("Running test");
    }

    @AfterAll
    public static void completeTransferRepository() {
        log.info("Test completed");
    }

    @BeforeEach
    public void initEachTestFourth() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
        user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        log.info(String.format("Классы %s, %s созданы",
                UserService.class, User.class));
    }

    @AfterEach
    public void afterEachTestFourth() {
        userService = null;
        user = null;
        log.info(String.format("Классы %s, %s обнулены",
                UserService.class, User.class));
    }

    @Test
    @DisplayName("Checking the getByUsernameTest method.")
    void getByUsernameTestFirst() {
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        assertEquals(user, userService.getByUsername(USERNAME));
    }
    @Test
    @DisplayName("Checking the addTokenAndUsername method (EXCEPTION).")
    void getByUsernameTestSecond() {
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername(USERNAME_2));
    }

}
