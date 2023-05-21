package ru.netology.cloudstorage.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.CloudFile;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.exception.IncorrectInputDataException;
import ru.netology.cloudstorage.exception.UnauthorizedException;
import ru.netology.cloudstorage.model.Request.NewFilenameRequest;
import ru.netology.cloudstorage.model.Response.ResponseFile;
import ru.netology.cloudstorage.model.Response.SuccessResponse;
import ru.netology.cloudstorage.repository.AuthorizationRepository;
import ru.netology.cloudstorage.repository.CloudRepository;
import ru.netology.cloudstorage.service.impl.CloudServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
public class CloudServiceTest {
    static final String USERNAME = "admin";
    static final String PASSWORD = "123qwe321";
    static final Long ID = 12L;
    static final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9";
    static final String ERROR_TOKEN = "123321qweewq";
    static final String TOKEN = BEARER_TOKEN.split(" ")[1];
    static final String FILENAME = "123.png";
    static final String ERROR_FILENAME = "321.png";
    static final String NEW_FILENAME = "777.png";
    static final Long SIZE = 22002L;
    static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    static final byte[] FILEDATA = FILENAME.getBytes();
    static final Integer LIMIT = 3;


    CloudService cloudService;
    @Mock
    CloudRepository cloudRepository;
    @Mock
    AuthorizationRepository repository;
    MultipartFile multipartFile;
    User user;
    CloudFile cloudFile;

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
        MockitoAnnotations.openMocks(this);
        cloudService = new CloudServiceImpl(cloudRepository, repository);
        multipartFile = new MockMultipartFile(FILENAME, FILEDATA);
        user = new User();
        cloudFile = new CloudFile(FILENAME, FILEDATA, SIZE, LOCAL_DATE_TIME, user.getId());
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setId(ID);
        Mockito.when(repository.getUsernameByToken(TOKEN)).thenReturn(Optional.ofNullable(user));
        Mockito.when(cloudRepository.findByUserIdAndFilename(user.getId(), FILENAME)).thenReturn(Optional.ofNullable(cloudFile));
        log.info(String.format("Классы %s, %s, %s, %s созданы",
                CloudService.class,
                MockMultipartFile.class,
                User.class,
                CloudFile.class));
    }

    @AfterEach
    void afterEachTestFourth() {
        cloudService = null;
        multipartFile = null;
        user = null;
        cloudFile = null;
        log.info(String.format("Классы %s, %s, %s, %s обнулены",
                CloudService.class,
                MockMultipartFile.class,
                User.class,
                CloudFile.class));
    }

    @Test
    @DisplayName("UPLOAD: Checking if a token is valid with an UnauthorizedException.")
    void uploadFileTestException() {
        assertThrows(UnauthorizedException.class, () -> cloudService.uploadFile(ERROR_TOKEN, FILENAME, multipartFile));
    }

    @Test
    @DisplayName("UPLOAD: Verifying successful upload of files to the database.")
    void uploadFileTestFirst() {
        assertNotNull(cloudService.uploadFile(TOKEN, NEW_FILENAME, multipartFile));
    }

    @Test
    @DisplayName("UPLOAD: Check for IncorrectInputDataException exception if file exists in database")
    void uploadFileTestSecond() {
        assertThrows(IncorrectInputDataException.class, () -> cloudService.uploadFile(TOKEN, FILENAME, multipartFile));
    }

    @Test
    @DisplayName("DELETE: Verifying successful delete of file from database.")
    void deleteFileTestFirst() {
        SuccessResponse successResponse = new SuccessResponse(String.format("File \"%s\" deleted successfully", FILENAME));
        assertEquals(successResponse, cloudService.deleteFile(TOKEN, FILENAME));
    }

    @Test
    @DisplayName("DELETE: Check for IncorrectInputDataException if file is not in database.")
    void deleteFileTestSecond() {
        assertThrows(IncorrectInputDataException.class, () -> cloudService.deleteFile(TOKEN, ERROR_FILENAME));
    }

    @Test
    @DisplayName("DELETE: Checking if a token is valid with an UnauthorizedException.")
    void deleteFileTestException() {
        assertThrows(UnauthorizedException.class, () -> cloudService.deleteFile(ERROR_TOKEN, FILENAME));
    }

    @Test
    @DisplayName("UPDATE: Verifying successful update of file from database.")
    void updateFileTestFirst() {
        NewFilenameRequest newFilenameRequest = new NewFilenameRequest();
        newFilenameRequest.setNewFilename(NEW_FILENAME);
        SuccessResponse successResponse = new SuccessResponse(
                String.format("Filename \"%s\" successfully updated to \"%s\"", FILENAME, newFilenameRequest.getNewFilename()));
        assertEquals(successResponse, cloudService.updateFile(TOKEN, FILENAME, newFilenameRequest));
    }

    @Test
    @DisplayName("UPDATE: Check for IncorrectInputDataException if file is not in database.")
    void updateFileTestSecond() {
        NewFilenameRequest newFilenameRequest = new NewFilenameRequest();
        newFilenameRequest.setNewFilename(NEW_FILENAME);
        assertThrows(IncorrectInputDataException.class, () -> cloudService.updateFile(TOKEN, ERROR_FILENAME, newFilenameRequest));
    }

    @Test
    @DisplayName("UPDATE: Checking if a token is valid with an UnauthorizedException.")
    void updateFileTestException() {
        NewFilenameRequest newFilenameRequest = new NewFilenameRequest();
        newFilenameRequest.setNewFilename(NEW_FILENAME);
        assertThrows(UnauthorizedException.class, () -> cloudService.updateFile(ERROR_TOKEN, FILENAME, newFilenameRequest));
    }

    @Test
    @DisplayName("DOWNLOAD: Verifying successful download of file from database.")
    void downloadFileTestFirst() {
        assertEquals(FILEDATA, cloudService.downloadFile(TOKEN, FILENAME));
    }

    @Test
    @DisplayName("DOWNLOAD: Check for IncorrectInputDataException if file is not in database.")
    void downloadFileTestSecond() {
        assertThrows(IncorrectInputDataException.class, () -> cloudService.downloadFile(TOKEN, ERROR_FILENAME));
    }

    @Test
    @DisplayName("DOWNLOAD: Checking if a token is valid with an UnauthorizedException.")
    void downloadFileTestException() {
        assertThrows(UnauthorizedException.class, () -> cloudService.downloadFile(ERROR_TOKEN, FILENAME));
    }

    @Test
    @DisplayName("GET ALL FILES: Checking the loading of all database files from the data.")
    void getAllFilesTestFirst() {
        ResponseFile responseFile = new ResponseFile(cloudFile.getFilename(), cloudFile.getSize());
        List<ResponseFile> fileList = List.of(responseFile);
        Mockito.when(cloudRepository.findAllByUserId(user.getId())).thenReturn(fileList);
        assertEquals(fileList, cloudService.getAllFiles(TOKEN, LIMIT));
    }

    @Test
    @DisplayName("GET ALL FILES: Checking if a token is valid with an UnauthorizedException.")
    void getAllFilesTestException() {
        assertThrows(UnauthorizedException.class, () -> cloudService.getAllFiles(ERROR_TOKEN, LIMIT));
    }
}
