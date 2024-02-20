package com.example.friends_service.repository;


import com.example.friends_service.model.Friend;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface FriendsRepository extends ReactiveCrudRepository<Friend,Long> {



    @Query("SELECT * FROM friends WHERE id_first_friend = :idFirstFriend AND id_second_friend = :idSecondFriend")
    Mono<Friend> findFriends(Long id_first_friend, Long idSecondFriend);
}
