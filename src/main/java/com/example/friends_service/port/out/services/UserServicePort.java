package com.example.friends_service.port.out.services;

import com.example.friends_service.model.api_models.IdUserDto;
import com.example.friends_service.model.api_models.UserDataDto;
import reactor.core.publisher.Mono;

public interface UserServicePort {
    Mono<UserDataDto>  getUserAboutId(IdUserDto idUserDtoData);
}
