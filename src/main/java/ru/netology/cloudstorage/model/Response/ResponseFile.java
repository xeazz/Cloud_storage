package ru.netology.cloudstorage.model.Response;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ResponseFile {
    private String filename;
    private Long size;
}
