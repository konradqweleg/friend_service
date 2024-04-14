package com.example.friends_service.services;

import com.example.friends_service.entity.request.*;
import com.example.friends_service.entity.response.IsFriends;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.entity.response.Status;
import com.example.friends_service.model.Friend;

import com.example.friends_service.port.in.FriendPort;
import com.example.friends_service.port.in.UserServicePort;
import com.example.friends_service.port.out.persistence.FriendsDatabasePort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FriendServices implements FriendPort {

    private final FriendsDatabasePort friendsDatabasePort;
    private final UserServicePort userServicePort;

    public FriendServices(FriendsDatabasePort friendsDatabasePort, UserServicePort userServicePort) {
        this.friendsDatabasePort = friendsDatabasePort;
        this.userServicePort = userServicePort;
    }


    private String USER_NOT_FOUND = "User not found";

    @Override
    public Mono<Result<Status>> createFriends(Mono<FriendsIdsData> friendsIdsMono) {

        return

                friendsIdsMono.flatMap(friendsIdsData ->
                        userServicePort.getUserAboutId(Mono.just(new IdUserData(friendsIdsData.idFirstFriend())))
                                .flatMap(userFirst -> userServicePort.getUserAboutId(Mono.just(new IdUserData(friendsIdsData.idSecondFriend())))
                                        .flatMap(userSecond -> {

                                            if (userFirst.isSuccess() && userSecond.isSuccess()) {
                                                return friendsDatabasePort.findFriendsByIds(new Friend(null, friendsIdsData.idFirstFriend(), friendsIdsData.idSecondFriend()))
                                                        .flatMap(foundFriendData -> {
                                                            if (foundFriendData != null) {
                                                                return Mono.just(Result.<Status>error("Friend already exists"));
                                                            } else {

                                                                return friendsDatabasePort.createFriend(new Friend(null, friendsIdsData.idFirstFriend(), friendsIdsData.idSecondFriend()))
                                                                        .thenReturn(Result.success(new Status(true)));
                                                            }
                                                        })
                                                        .switchIfEmpty(
                                                                friendsDatabasePort.createFriend(new Friend(null, friendsIdsData.idFirstFriend(), friendsIdsData.idSecondFriend()))
                                                                        .thenReturn(Result.success(new Status(true)))
                                                        )
                                                        .onErrorResume(throwable -> Mono.just(Result.error(throwable.getMessage())));
                                            } else {
                                                return Mono.just(Result.<Status>error(USER_NOT_FOUND));
                                            }
                                        })
                                )
                                .switchIfEmpty(Mono.just(Result.<Status>error(USER_NOT_FOUND)))
                );


    }

    @Override
    public Mono<Result<IsFriends>> isFriends(Mono<FriendsIdsData> friendsIdsMono) {
        return friendsIdsMono
                .flatMap(friendsIdsData -> friendsDatabasePort.findFriendsByIds(new Friend(null, friendsIdsData.idFirstFriend(), friendsIdsData.idSecondFriend()))
                        .map(friend -> new IsFriends(true))
                        .defaultIfEmpty(new IsFriends(false))
                        .map(Result::success))
                .onErrorResume(throwable -> Mono.just(Result.error(throwable.getMessage())));
    }

    @Override
    public Flux<UserData> getFriends(Mono<IdUser> idUserMono) {
        return idUserMono
                .flatMapMany(idUser -> friendsDatabasePort.findFriendsUser(idUser.idUser())
                        .flatMap(friend -> {
                            if (friend.idFirstFriend().equals(idUser.idUser())) {
                                return userServicePort.getUserAboutId(Mono.just(new IdUserData(friend.idSecondFriend())))
                                        .map(user -> new UserData(friend.idSecondFriend(),user.getValue().name(),user.getValue().surname(),user.getValue().email() ));
                            } else {
                                return userServicePort.getUserAboutId(Mono.just(new IdUserData(friend.idFirstFriend())))
                                        .map(user -> new UserData(friend.idFirstFriend(),user.getValue().name(),user.getValue().surname(),user.getValue().email()));
                            }
                        })
                )
                .onErrorResume(throwable -> Flux.empty());
    }
}
