package com.example.friends_service.model.api_models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

public record UserDataDto(@Id Long id, @NotNull @Min(2) String name, @NotNull @Min(2) String surname, @Email String email) {
}
