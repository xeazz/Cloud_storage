package ru.netology.cloudstorage.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.netology.cloudstorage.service.CloudService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudServiceImpl implements CloudService {
    private final CloudRepository cloudRepository;

    public final AuthorizationRepository repository;

    @Override
    @Transactional
    public SuccessResponse uploadFile(String authToken, String filename, MultipartFile file) {
        log.info("Start to upload file \"{}\"", filename);
        final User user = getUserByToken(authToken);
        final Optional<CloudFile> isExist = cloudRepository.findByUserIdAndFilename(user.getId(), filename);
        if (isExist.isPresent()) {
            log.info("Failed upload file: A file with the same name already exists");
            throw new IncorrectInputDataException("Failed upload file: A file with the same name already exists");
        }
        try {
            cloudRepository.save(CloudFile
                    .builder()
                    .filename(filename)
                    .fileData(file.getBytes())
                    .size(file.getSize())
                    .uploadDate(LocalDateTime.now())
                    .userId(user.getId())
                    .build());
            log.info("Success upload file. Username {}", user.getUsername());
            return SuccessResponse.builder().message("Success upload file").build();
        } catch (IOException e) {
            log.error("Failed upload file: Input data exception");
            throw new IncorrectInputDataException("Failed upload file: Input data exception");
        }
    }

    @Override
    @Transactional
    public SuccessResponse deleteFile(String authToken, String filename) {
        log.info("Start to delete file \"{}\"", filename);
        final User user = getUserByToken(authToken);
        final Optional<CloudFile> isExist = cloudRepository.findByUserIdAndFilename(user.getId(), filename);
        if (isExist.isEmpty()) {
            log.info("Failed delete file: Failed to delete file because it is missing");
            throw new IncorrectInputDataException("Failed delete file: Failed to delete file because it is missing");
        }
        cloudRepository.deleteByUserIdAndFilename(user.getId(), filename);
        log.info("File \"{}\" deleted successfully", filename);
        return SuccessResponse
                .builder()
                .message(String.format("File \"%s\" deleted successfully", filename))
                .build();
    }

    @Override
    @Transactional
    public SuccessResponse updateFile(String authToken, String filename, NewFilenameRequest newFilenameRequest) {
        log.info("Start to update file \"{}\"", filename);
        final User user = getUserByToken(authToken);
        final Optional<CloudFile> cloudFile = cloudRepository.findByUserIdAndFilename(user.getId(), filename);
        if (cloudFile.isEmpty()) {
            log.info("Failed update file: The file is missing and cannot be updated");
            throw new IncorrectInputDataException("Failed update file: The file is missing and cannot be updated");
        }
        cloudRepository.updateFilenameByUser(user.getId(), filename, newFilenameRequest.getNewFilename());
        log.info("Filename \"{}\" successfully updated to \"{}\"", filename, newFilenameRequest.getNewFilename());
        return SuccessResponse
                .builder()
                .message(String.format("Filename \"%s\" successfully updated to \"%s\"",
                        filename,
                        newFilenameRequest.getNewFilename()))
                .build();
    }

    @Override
    @Transactional
    public byte[] downloadFile(String authToken, String filename) {
        log.info("Start to download file \"{}\"", filename);
        final User user = getUserByToken(authToken);
        final Optional<CloudFile> cloudFile = cloudRepository.findByUserIdAndFilename(user.getId(), filename);
        if (cloudFile.isEmpty()) {
            log.info("Failed download file: The file is missing and cannot be downloaded");
            throw new IncorrectInputDataException("Failed download file: The file is missing and cannot be downloaded");
        }
        log.info("File \"{}\" downloaded successfully", filename);
        return cloudFile.get().getFileData();

    }

    @Override
    public List<ResponseFile> getAllFiles(String authToken, Integer limit) {
        final User user = getUserByToken(authToken);
        log.info("Success get all files. User {}", user.getUsername());
        return cloudRepository.findAllByUserId(user.getId()).stream()
                .map(o -> ResponseFile.builder()
                        .filename(o.getFilename())
                        .size(o.getSize()).build())
                .collect(Collectors.toList());
    }

    private User getUserByToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        final Optional<User> user = repository.getUsernameByToken(authToken);
        if (user.isPresent()) {
            log.info("Working with files: Authorized successfully");
            return user.get();
        }
        log.info("Working with files: Unauthorized Error");
        throw new UnauthorizedException("Unauthorized error");
    }
}
