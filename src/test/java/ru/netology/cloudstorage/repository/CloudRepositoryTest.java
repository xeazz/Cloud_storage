package ru.netology.cloudstorage.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.netology.cloudstorage.entity.CloudFile;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.model.Response.ResponseFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class CloudRepositoryTest {
    static final String USERNAME = "admin";
    static final String USERNAME_2 = "user";
    static final String PASSWORD = "123qwe321";
    static final String FILENAME = "123.png";
    static final String FILENAME_2 = "321.png";
    static final String NEW_FILENAME = "111.png";
    static final Long SIZE = 22002L;
    static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    static final byte[] FILEDATA = FILENAME.getBytes();
    static final byte[] FILEDATA_2 = FILENAME_2.getBytes();
    @Autowired
    CloudRepository cloudRepository;
    User user;
    @Cascade(value = CascadeType.ALL)
    CloudFile cloudFileFirst;
    @Cascade(value = CascadeType.ALL)
    CloudFile cloudFileSecond;
    @Cascade(value = CascadeType.ALL)
    CloudFile cloudFileNew;

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
        cloudFileFirst = new CloudFile(FILENAME, FILEDATA, SIZE, LOCAL_DATE_TIME, user.getId());
        cloudFileSecond = new CloudFile(FILENAME_2, FILEDATA_2, SIZE, LOCAL_DATE_TIME, user.getId());
        cloudFileNew = new CloudFile(NEW_FILENAME, FILEDATA, SIZE, LOCAL_DATE_TIME, user.getId());
        cloudRepository.deleteAll();
        cloudRepository.save(cloudFileFirst);
        cloudRepository.save(cloudFileSecond);
        log.info(String.format("Класс %s создан", User.class));
    }

    @AfterEach
    void afterEachTestFourth() {
        cloudRepository = null;
        user = null;
        cloudFileFirst = null;
        cloudFileSecond = null;
        log.info(String.format("Классы %s обнулен", User.class));
    }

    @Test
    @DisplayName("Finding all \"CloudFile\" entities.")
    void findAllByUserTestFirst() {
        ResponseFile responseFileFirst = new ResponseFile(cloudFileFirst.getFilename(), cloudFileFirst.getSize());
        ResponseFile responseFileSecond = new ResponseFile(cloudFileSecond.getFilename(), cloudFileSecond.getSize());
        List<ResponseFile> fileList = List.of(responseFileFirst, responseFileSecond);
        assertEquals(fileList, cloudRepository.findAllByUserId(user.getId()));
    }
    @Test
    @DisplayName("Finding all \"CloudFile\" entities. Unfavorable outcome.")
    void findAllByUserTestSecond() {
        ResponseFile responseFileSecond = new ResponseFile(cloudFileSecond.getFilename(), cloudFileSecond.getSize());
        List<ResponseFile> fileList = List.of(responseFileSecond);
        assertNotEquals(fileList, cloudRepository.findAllByUserId(user.getId()));
    }

    @Test
    @DisplayName("Search for CloudFile entities by user and file name.")
    void findByUserAndFilenameTestFirst() {
        assertEquals(Optional.ofNullable(cloudFileFirst),
                cloudRepository.findByUserIdAndFilename(user.getId(), FILENAME));
    }

    @Test
    @DisplayName("Search for CloudFile entities by user and file name." +
            "When an unexpected CloudFile entity is returned.")
    void findByUserAndFilenameTestSecond() {
        assertNotEquals(Optional.ofNullable(cloudFileSecond),
                cloudRepository.findByUserIdAndFilename(user.getId(), FILENAME));
    }

    @Test
    @DisplayName("Deleting the CloudFile entity by user and file name.")
    void deleteByUserAndFilenameTest() {
        List<CloudFile> cloudFileList = List.of(cloudFileFirst, cloudFileSecond);
        cloudRepository.deleteByUserIdAndFilename(user.getId(), FILENAME);
        List<CloudFile> cloudFileAfterDelete = cloudRepository.findAll();
        assertNotEquals(cloudFileList, cloudFileAfterDelete);
    }

    @Test
    @DisplayName("Deleting the CloudFile entity by user and file name.")
    void updateFilenameByUserTest() {
        Optional<CloudFile> beforeDelete = cloudRepository.findById(FILENAME);
        assertTrue(beforeDelete.isPresent());
        cloudRepository.deleteByUserIdAndFilename(user.getId(), FILENAME);
        Optional<CloudFile> afterDelete = cloudRepository.findById(FILENAME);
        assertFalse(afterDelete.isPresent());
    }

}
