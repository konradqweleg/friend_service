package com.example.friends_service.port.out.persistence;

import com.example.friends_service.model.api_models.IdUserDto;

import com.example.friends_service.model.api_models.FriendRelationDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersistencePort {
    Mono<FriendRelationDto> createFriend(FriendRelationDto friendRelationToCreate);

    Mono<FriendRelationDto> findFriendsRelation(FriendRelationDto friendsId);

    Flux<FriendRelationDto> findUserFriends(IdUserDto idUserDto);
}
