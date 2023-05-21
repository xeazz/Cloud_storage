package ru.netology.cloudstorage.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class NewFilenameRequest {
    @JsonProperty("filename")
    private String newFilename;
}
