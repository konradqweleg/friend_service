package com.example.friends_service.port.in;

import com.example.friends_service.entity.request.FriendData;
import com.example.friends_service.entity.response.IsFriends;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.entity.response.Status;
import reactor.core.publisher.Mono;

public interface FriendPort {
    Mono<Result<Status>> createFriends(Mono<FriendData> friendsIdsMono);
    Mono<Result<IsFriends>> isFriends(Mono<FriendData> friendsIdsMono);
}
