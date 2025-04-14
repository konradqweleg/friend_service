package com.example.friends_service.port.in;

import com.example.friends_service.model.api_models.IdUserDto;
import com.example.friends_service.model.api_models.UserDataDto;
import com.example.friends_service.model.api_models.IsFriendsDto;
import com.example.friends_service.model.api_models.FriendRelationDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FriendPort {
    Mono<Void> createFriends(FriendRelationDto newFriendsIds);
    Mono<IsFriendsDto> isFriends(FriendRelationDto friendsIdsMono);
    Flux<UserDataDto> getFriends(IdUserDto idUserDtoMono);

}
