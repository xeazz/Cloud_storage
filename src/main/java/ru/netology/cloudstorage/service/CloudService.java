package ru.netology.cloudstorage.service;

import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.model.Request.NewFilenameRequest;
import ru.netology.cloudstorage.model.Response.ResponseFile;
import ru.netology.cloudstorage.model.Response.SuccessResponse;

import java.util.List;

public interface CloudService {
    SuccessResponse uploadFile(String authToken, String filename, MultipartFile file);

    SuccessResponse deleteFile(String authToken, String filename);

    SuccessResponse updateFile(String authToken, String filename, NewFilenameRequest newFilenameRequest);

    byte[] downloadFile(String authToken, String filename);

    List<ResponseFile> getAllFiles(String authToken, Integer limit);


}
