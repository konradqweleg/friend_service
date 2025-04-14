package com.example.friends_service.repository;


import com.example.friends_service.model.db.Friend;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface FriendsRepository extends ReactiveCrudRepository<Friend,Long> {
    @Query("SELECT * FROM friends" +
            " WHERE (id_first_friend = :idFirstFriend AND id_second_friend = :idSecondFriend) " +
            "or (id_first_friend = :idSecondFriend AND id_second_friend = :idFirstFriend)"
    )
    Mono<Friend> findFriends(Long id_first_friend, Long idSecondFriend);

    @Query("SELECT * FROM friends WHERE (id_first_friend = :idUser OR id_second_friend = :idUser)")
    Flux<Friend> findFriendsUser(Long idUser);
}
