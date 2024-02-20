package com.example.friends_service.port.out.persistence;

import com.example.friends_service.model.Friend;

import reactor.core.publisher.Mono;

public interface FriendsDatabasePort {
    Mono<Friend> createFriend(Friend friendsId);

    Mono<Friend> findFriendsByIds(Friend friendsId);
}
