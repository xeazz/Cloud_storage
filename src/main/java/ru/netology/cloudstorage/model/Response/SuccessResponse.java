package ru.netology.cloudstorage.model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SuccessResponse {
    private String message;
}
