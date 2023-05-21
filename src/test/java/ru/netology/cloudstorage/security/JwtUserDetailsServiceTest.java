package ru.netology.cloudstorage.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.netology.cloudstorage.entity.Role;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.repository.UserRepository;
import ru.netology.cloudstorage.security.model.JwtUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class JwtUserDetailsServiceTest {
    static final String USERNAME = "admin";
    static final String USERNAME_2 = "user";
    static final String PASSWORD = "123qwe321";
    static final String ROLE_NAME = "ADMIN";
    static final Long ID = 12L;
    JwtUserDetailsService jwtUserDetailsService;
    @Mock
    UserRepository userRepository;
    JwtUser jwtUser;
    User user;
    Role role;


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
        jwtUserDetailsService = new JwtUserDetailsService(userRepository);
        user = new User();
        role = new Role();
        jwtUser = new JwtUser();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        role.setId(ID);
        role.setName(ROLE_NAME);
        role.setUsers(List.of(user));
        log.info(String.format("Классы %s, %s, %s, %s созданы",
                JwtUserDetailsService.class, User.class, Role.class, JwtUser.class));
    }

    @AfterEach
    public void afterEachTestFourth() {
        jwtUserDetailsService = null;
        user = null;
        role = null;
        jwtUser = null;
        log.info(String.format("Классы %s, %s, %s, %s обнулены",
                JwtUserDetailsService.class, User.class, Role.class, JwtUser.class));
    }


    @Test
    @DisplayName("Checking the loadUserByUsername method. Favorable outcome.")
    void loadUserByUsernameTestFirst() {
        List<SimpleGrantedAuthority> grantedAuthorityList = List.of(new SimpleGrantedAuthority(ROLE_NAME));
        jwtUser.setUsername(USERNAME);
        jwtUser.setPassword(PASSWORD);
        jwtUser.setId(ID);
        jwtUser.setAuthorities(grantedAuthorityList);

        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        assertEquals(jwtUser, jwtUserDetailsService.loadUserByUsername(USERNAME));
    }

    @Test
    @DisplayName("Checking the loadUserByUsername method. Unfavorable outcome.")
    void loadUserByUsernameTestSecond() {
        List<SimpleGrantedAuthority> grantedAuthorityList = List.of(new SimpleGrantedAuthority(ROLE_NAME));
        jwtUser.setUsername(USERNAME_2);
        jwtUser.setPassword(PASSWORD);
        jwtUser.setId(ID);
        jwtUser.setAuthorities(grantedAuthorityList);

        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        assertNotEquals(jwtUser, jwtUserDetailsService.loadUserByUsername(USERNAME));
    }

    @Test
    @DisplayName("Checking the loadUserByUsername method (EXCEPTION).")
    void loadUserByUsernameTestThird() {
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        assertThrows(UsernameNotFoundException.class, () -> jwtUserDetailsService.loadUserByUsername(USERNAME_2));
    }
}
