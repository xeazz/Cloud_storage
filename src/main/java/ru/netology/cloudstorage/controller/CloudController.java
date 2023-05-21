package ru.netology.cloudstorage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.model.Request.NewFilenameRequest;
import ru.netology.cloudstorage.model.Response.ResponseFile;
import ru.netology.cloudstorage.model.Response.SuccessResponse;
import ru.netology.cloudstorage.service.CloudService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CloudController {
    private final CloudService cloudService;

    @GetMapping("/list")
    public ResponseEntity<List<ResponseFile>> getAllFiles(@RequestHeader("auth-token") String authToken,
                                                          @RequestParam("limit") Integer limit) {
        return ResponseEntity.ok(cloudService.getAllFiles(authToken, limit));
    }

    @PostMapping("/file")
    public ResponseEntity<SuccessResponse> uploadFile(@RequestHeader("auth-token") String authToken,
                                                      @RequestParam("filename") String fileName,
                                                      @RequestPart MultipartFile file) {
        return ResponseEntity.ok().body(cloudService.uploadFile(authToken, fileName, file));
    }


    @DeleteMapping("/file")
    public ResponseEntity<SuccessResponse> deleteFile(@RequestHeader("auth-token") String authToken,
                                                      @RequestParam("filename") String filename) {
        return ResponseEntity.ok().body(cloudService.deleteFile(authToken, filename));
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String filename) {
        return ResponseEntity.ok().body(cloudService.downloadFile(authToken, filename));
    }

    @PutMapping("/file")
    public ResponseEntity<SuccessResponse> updateFile(@RequestHeader("auth-token") String authToken,
                                                      @RequestParam("filename") String filename,
                                                      @RequestBody NewFilenameRequest newFilenameRequest) {
        return ResponseEntity.ok().body(cloudService.updateFile(authToken, filename, newFilenameRequest));
    }
}
