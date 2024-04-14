package com.example.friends_service.port.in;

import com.example.friends_service.entity.request.FriendIdData;
import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.entity.request.IdUser;
import com.example.friends_service.entity.request.UserData;
import com.example.friends_service.entity.response.IsFriends;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.entity.response.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FriendPort {
    Mono<Result<Status>> createFriends(Mono<FriendsIdsData> friendsIdsMono);
    Mono<Result<IsFriends>> isFriends(Mono<FriendsIdsData> friendsIdsMono);

    Flux<UserData> getFriends(Mono<IdUser> idUserMono);


}
