package com.example.friends_service.adapter.out.persistence;

import com.example.friends_service.model.Friend;

import com.example.friends_service.port.out.persistence.FriendsDatabasePort;
import com.example.friends_service.repository.FriendsRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostgresFriendRepository implements FriendsDatabasePort {

    private FriendsRepository friendsRepository;

    public PostgresFriendRepository(FriendsRepository friendsRepository) {
        this.friendsRepository = friendsRepository;
    }


    @Override
    public Mono<Friend> createFriend(Friend friendsId) {
        return friendsRepository.save(friendsId);
    }

    @Override

    public Mono<Friend> findFriendsByIds(Friend friendsId) {
        return friendsRepository.findFriends(friendsId.idFirstFriend(), friendsId.idSecondFriend());
    }

    @Override
    public Flux<Friend> findFriendsUser(Long idUser) {
        return friendsRepository.findFriendsUser(idUser);
    }
}
