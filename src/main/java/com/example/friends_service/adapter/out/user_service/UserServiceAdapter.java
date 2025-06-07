package com.example.friends_service.adapter.out.user_service;

import com.example.friends_service.exceptions.*;
import com.example.friends_service.model.api_models.IdUserDto;
import com.example.friends_service.model.api_models.UserDataDto;
import com.example.friends_service.port.out.services.UserServicePort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class UserServiceAdapter implements UserServicePort {
    @Value("${user.service.url}")
    private String mainUserServiceUrl;

    private final Logger logger = LogManager.getLogger(UserServiceAdapter.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebClient webClient;

    public UserServiceAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<UserDataDto> getUserAboutId(IdUserDto idUserDtoData) {
        String uri = UriComponentsBuilder.fromUriString(mainUserServiceUrl)
                .path("/{id}")
                .buildAndExpand(idUserDtoData.idUser().toString())
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> {
                            if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                                return Mono.error(new UserNotFoundException("User not found with ID: " + idUserDtoData.idUser()));
                            } else {
                                return response.bodyToMono(String.class)
                                        .flatMap(body -> Mono.error(new UserServiceUnexpectedException("Error from user service: " + body)));
                            }
                        }
                )
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    try {
                        UserDataDto userDataDto = objectMapper.readValue(responseBody, UserDataDto.class);
                        return Mono.just(userDataDto);
                    } catch (JsonProcessingException e) {
                        logger.error("Error parsing response from user service: {}", e.getMessage());
                        return Mono.error(new UserServiceUnexpectedException("Error parsing JSON response"));
                    }
                })
                .onErrorResume(e -> {
                    logger.error("Error occurred: {}", e.getMessage());
                    return Mono.error(new UserServiceUnexpectedException("Unexpected error"));
                });
    }

}
