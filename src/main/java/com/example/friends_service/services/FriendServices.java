package com.example.friends_service.services;

import com.example.friends_service.entity.request.FriendData;
import com.example.friends_service.entity.response.IsFriends;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.entity.response.Status;
import com.example.friends_service.model.Friend;

import com.example.friends_service.port.in.FriendPort;
import com.example.friends_service.port.out.persistence.FriendsDatabasePort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FriendServices implements FriendPort {

    private final FriendsDatabasePort friendsDatabasePort;

    public FriendServices(FriendsDatabasePort friendsDatabasePort) {
        this.friendsDatabasePort = friendsDatabasePort;
    }


    @Override
    public Mono<Result<Status>> createFriends(Mono<FriendData> friendsIdsMono) {
        return friendsIdsMono
                .flatMap(friendData -> friendsDatabasePort.createFriend(new Friend(null,friendData.idFirstFriend(), friendData.idSecondFriend()))
                        .thenReturn(Result.success(new Status(true))))
                        .onErrorResume(throwable -> Mono.just(Result.error(throwable.getMessage())));
    }

    @Override
    public Mono<Result<IsFriends>> isFriends(Mono<FriendData> friendsIdsMono) {
        return friendsIdsMono
                .flatMap(friendData -> friendsDatabasePort.findFriendsByIds(new Friend(null,friendData.idFirstFriend(), friendData.idSecondFriend()))
                        .map(friend -> new IsFriends(true))
                        .defaultIfEmpty(new IsFriends(false))
                        .map(Result::success))
                .onErrorResume(throwable -> Mono.just(Result.error(throwable.getMessage())));
    }
}
