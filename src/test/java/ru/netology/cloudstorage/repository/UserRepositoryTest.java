package ru.netology.cloudstorage.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.netology.cloudstorage.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    static final String USERNAME = "admin";
    static final String USERNAME_2 = "user";
    static final String PASSWORD = "123qwe321";
    @Autowired
    UserRepository userRepository;
    User user;

    @BeforeAll
    static void initTransferRepository() {
        log.info("Running test");
    }

    @AfterAll
    static void completeTransferRepository() {
        log.info("Test completed");
    }

    @BeforeEach
    void initEachTestFourth() {
        user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        userRepository.deleteAll();
        userRepository.save(user);
        log.info(String.format("Класс %s создан", User.class));
    }

    @AfterEach
    void afterEachTestFourth() {
        userRepository = null;
        user = null;
        log.info(String.format("Класс %s обнулен", User.class));
    }

    @Test
    @DisplayName("The database contains the searched user.")
    void findByUsernameFirst() {

        assertEquals(Optional.ofNullable(user), userRepository.findByUsername(USERNAME));
    }
    @Test
    @DisplayName("The database does not contain the user you are looking for")
    void findByUsernameSecond() {
        assertNotEquals(Optional.ofNullable(user), userRepository.findByUsername(USERNAME_2));
    }

}
