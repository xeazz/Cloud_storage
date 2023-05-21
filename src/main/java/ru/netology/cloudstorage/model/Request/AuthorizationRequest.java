package ru.netology.cloudstorage.model.Request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationRequest {
    @NotBlank(message = "login must not be empty")
    private String login;
    @NotBlank(message = "password must not be empty")
    private String password;
}
