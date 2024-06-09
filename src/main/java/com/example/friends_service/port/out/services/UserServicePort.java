package com.example.friends_service.port.out.services;

import com.example.friends_service.entity.request.IdUserData;
import com.example.friends_service.entity.request.UserData;
import com.example.friends_service.entity.response.Result;
import reactor.core.publisher.Mono;

public interface UserServicePort {
    Mono<Result<UserData>>  getUserAboutId(Mono<IdUserData> idUserDataMono);
}
