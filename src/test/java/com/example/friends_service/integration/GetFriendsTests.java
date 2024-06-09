package com.example.friends_service.integration;

import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.entity.request.IdUser;
import com.example.friends_service.entity.request.UserData;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.entity.response.Status;
import com.example.friends_service.integration.mocks.UserServicePortMock;
import com.example.friends_service.port.in.FriendPort;
import com.example.friends_service.port.out.services.UserServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URISyntaxException;


public class GetFriendsTests extends DefaultIntegrationTestConfiguration {

    @MockBean
    private UserServicePort userServicePort;

    @Autowired
    private FriendPort friendPort;

    private final Long idUser = 1L;
    private final UserServicePortMock userServicePortMock = new UserServicePortMock();

    private void createFriends(Long idFirstUser, Long idSecondUser) {
        FriendsIdsData friendsIdsData = new FriendsIdsData(idFirstUser, idSecondUser);
        Mono<Result<Status>> monoCreateFriends = friendPort.createFriends(Mono.just(friendsIdsData));
        monoCreateFriends.block();
    }

    @Test
    public void whenUserHasFriendsRequestShouldReturnFriends() throws URISyntaxException {
        // given
        userServicePortMock.mockGetUserById(userServicePort);
        Long idFirstUserFriend = 2L;
        Long idSecondUserFriend = 3L;
        createFriends(idUser, idFirstUserFriend);
        createFriends(idUser, idSecondUserFriend);

        // when
        Flux<UserData> friends = friendPort.getFriends(Mono.just(new IdUser(idUser)));

        // then
        StepVerifier.create(friends)
                .expectNextMatches(user -> user.id().equals(idFirstUserFriend))
                .expectNextMatches(user -> user.id().equals(idSecondUserFriend))
                .expectComplete()
                .verify();

    }

    @Test
    public void whenUserHasNoFriendsRequestShouldReturnEmptyList() {
        // given
        userServicePortMock.mockGetUserById(userServicePort);

        // when
        Flux<UserData> friends = friendPort.getFriends(Mono.just(new IdUser(idUser)));

        // then
        long expectNoFriends = 0L;
        StepVerifier.create(friends)
                .expectNextCount(expectNoFriends)
                .expectComplete()
                .verify();

    }
}
