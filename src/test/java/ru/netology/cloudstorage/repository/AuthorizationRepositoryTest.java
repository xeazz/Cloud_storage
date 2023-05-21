package ru.netology.cloudstorage.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.repository.impl.AuthorizationRepositoryImpl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class AuthorizationRepositoryTest {
    private static final String TOKEN_FOR_REPOSITORY = "eyJhbGciOiJIUzI1NiJ9";
    private static final String TOKEN_FOR_TESTMAP = "eyJhbGciOiJIUzI1NiJ1";
    static final String USERNAME = "admin";
    static final String USERNAME_2 = "user";
    static final String PASSWORD = "123qwe321";
    static final String PASSWORD_2 = "333aaa333";
    static final Long ID = 12L;
    static final Long ID_2 = 1L;
    private final Map<String, User> testMap = new ConcurrentHashMap<>();
    AuthorizationRepository repository;
    User userFirst;
    User userSecond;


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
        repository = new AuthorizationRepositoryImpl();
        userFirst = new User();
        userSecond = new User();
        log.info(String.format("Классы %s, %s созданы",
                AuthorizationRepository.class,
                User.class));
    }

    @AfterEach
    public void afterEachTestFourth() {
        repository = null;
        userFirst = null;
        userSecond = null;
        log.info(String.format("Классы %s, %s обнулены",
                AuthorizationRepository.class,
                User.class));
    }

    @Test
    @DisplayName("Checking the addTokenAndUsername method. Success writing to repository.")
    void addTokenAndUsernameTestFirst() {
        userFirst.setId(ID);
        userFirst.setUsername(USERNAME);
        userFirst.setPassword(PASSWORD);
        testMap.putIfAbsent(TOKEN_FOR_REPOSITORY, userFirst);
        repository.addTokenAndUsername(TOKEN_FOR_REPOSITORY, userFirst);
        assertEquals(testMap, repository.getPersonMap());
    }

    @Test
    @DisplayName("Checking the addTokenAndUsername method. Error writing to repository.")
    void addTokenAndUsernameTestSecond() {
        userFirst.setId(ID);
        userFirst.setUsername(USERNAME);
        userFirst.setPassword(PASSWORD);
        testMap.putIfAbsent(TOKEN_FOR_TESTMAP, userFirst);
        repository.addTokenAndUsername(TOKEN_FOR_REPOSITORY, userFirst);
        assertNotEquals(testMap, repository.getPersonMap());
    }

    @Test
    @DisplayName("Checking the getUsernameByToken method. Success read from repository.")
    void getUsernameByTokenTestFirst() {
        userFirst.setId(ID);
        userFirst.setUsername(USERNAME);
        userFirst.setPassword(PASSWORD);

        userSecond.setId(ID);
        userSecond.setUsername(USERNAME);
        userSecond.setPassword(PASSWORD);

        repository.addTokenAndUsername(TOKEN_FOR_REPOSITORY, userFirst);
        assertEquals(Optional.ofNullable(userSecond), repository.getUsernameByToken(TOKEN_FOR_REPOSITORY));
    }

    @Test
    @DisplayName("Checking the getUsernameByToken method. Error read from repository.")
    void getUsernameByTokenTestSecond() {
        userFirst.setId(ID);
        userFirst.setUsername(USERNAME);
        userFirst.setPassword(PASSWORD);

        userSecond.setId(ID_2);
        userSecond.setUsername(USERNAME_2);
        userSecond.setPassword(PASSWORD_2);

        repository.addTokenAndUsername(TOKEN_FOR_REPOSITORY, userFirst);
        assertNotEquals(Optional.ofNullable(userSecond), repository.getUsernameByToken(TOKEN_FOR_REPOSITORY));
    }

    @Test
    @DisplayName("Checking the getUsernameByToken method. When response is empty")
    void getUsernameByTokenTestThird() {
        userFirst.setId(ID);
        userFirst.setUsername(USERNAME);
        userFirst.setPassword(PASSWORD);

        repository.addTokenAndUsername(TOKEN_FOR_REPOSITORY, userFirst);
        assertEquals(Optional.empty(), repository.getUsernameByToken(TOKEN_FOR_TESTMAP));
    }

    @Test
    @DisplayName("Checking the removeTokenAndUsernameByToken method.")
    void removeTokenAndUsernameByTokenTest() {
        userFirst.setId(ID);
        userFirst.setUsername(USERNAME);
        userFirst.setPassword(PASSWORD);

        userSecond.setId(ID_2);
        userSecond.setUsername(USERNAME_2);
        userSecond.setPassword(PASSWORD_2);

        testMap.putIfAbsent(TOKEN_FOR_REPOSITORY, userFirst);
        repository.addTokenAndUsername(TOKEN_FOR_REPOSITORY, userFirst);
        repository.addTokenAndUsername(TOKEN_FOR_TESTMAP, userSecond);
        repository.removeTokenAndUsernameByToken(TOKEN_FOR_TESTMAP);
        assertEquals(testMap, repository.getPersonMap());
    }
}
