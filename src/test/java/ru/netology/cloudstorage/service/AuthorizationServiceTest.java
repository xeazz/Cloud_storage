package ru.netology.cloudstorage.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.netology.cloudstorage.entity.Role;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.model.Request.AuthorizationRequest;
import ru.netology.cloudstorage.model.Response.AuthorizationResponse;
import ru.netology.cloudstorage.repository.AuthorizationRepository;
import ru.netology.cloudstorage.security.JwtTokenProvider;
import ru.netology.cloudstorage.service.impl.AuthorizationServiceImpl;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class AuthorizationServiceTest {
    static final String USERNAME = "admin";
    static final String PASSWORD = "123qwe321";
    static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9";
    static final String ROLE_NAME = "ADMIN";
    static final Long ID = 12L;
    static final Long ID_2 = 1L;
    AuthorizationService service;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    AuthorizationRepository authorizationRepository;
    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    UserService userService;

    User user;
    Role role;
    AuthorizationResponse authorizationResponse;
    AuthorizationRequest authorizationRequest;


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
        service = new AuthorizationServiceImpl(
                authenticationManager,
                authorizationRepository,
                jwtTokenProvider,
                userService);
        user = new User();
        role = new Role();
        authorizationRequest = new AuthorizationRequest();
        authorizationResponse = new AuthorizationResponse();
        log.info(String.format("Классы %s, %s, %s, %s, %s созданы",
                AuthorizationService.class,
                User.class,
                Role.class,
                AuthorizationRequest.class,
                AuthorizationResponse.class));
    }

    @AfterEach
    public void afterEachTestFourth() {
        service = null;
        user = null;
        role = null;
        authorizationRequest = null;
        authorizationResponse = null;
        log.info(String.format("Классы %s, %s, %s, %s, %s обнулены",
                AuthorizationService.class,
                User.class,
                Role.class,
                AuthorizationRequest.class,
                AuthorizationResponse.class));
    }

    @Test
    @DisplayName("Authorization method check.")
    void loginTestFirst() {

        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));

        role.setId(ID_2);
        role.setName(ROLE_NAME);
        role.setUsers(List.of(user));

        authorizationResponse.setToken(TOKEN);
        Mockito.when(userService.getByUsername(authorizationRequest.getPassword())).thenReturn(user);
        Mockito.when(jwtTokenProvider.generateToken(user.getUsername(), user.getRoles())).thenReturn(TOKEN);
        assertEquals(authorizationResponse, service.login(authorizationRequest));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        authorizationRequest.getLogin(),
                        authorizationRequest.getPassword());
        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(usernamePasswordAuthenticationToken);
        Mockito.verify(authorizationRepository, Mockito.times(1)).addTokenAndUsername(TOKEN, user);
    }

}
