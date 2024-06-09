package com.example.friends_service.integration;

import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.entity.response.IsFriends;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.entity.response.Status;
import com.example.friends_service.integration.mocks.UserServicePortMock;
import com.example.friends_service.port.in.FriendPort;
import com.example.friends_service.port.out.services.UserServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URISyntaxException;


public class IsFriendsTest extends DefaultIntegrationTestConfiguration {

    private final Long idFirstUser = 1L;
    private final Long idSecondUser = 2L;

    @MockBean
    private UserServicePort userServicePort;

    @Autowired
    private FriendPort friendPort;
    private UserServicePortMock userServicePortMock = new UserServicePortMock();
//    @Test
//    public void whenUsersAreFriendsShouldReturnUsersAreFriends() throws URISyntaxException {
//        // given
//        userServicePortMock.mockGetUserById(userServicePort);
//
//        FriendsIdsData friendsIdsData = new FriendsIdsData(idFirstUser, idSecondUser);
//        webTestClient.post().uri(createRequestUtil().createRequestCreateFriends())
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(friendsIdsData))
//                .exchange();
//
//        // when
//        // then
//        webTestClient.get().uri(createRequestUtil().createRequestIsFriends(idFirstUser, idSecondUser))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.areFriends").isEqualTo(true);
//
//    }

    @Test
    public void whenUsersAreNotFriendsShouldReturnUsersAreNotFriends() throws URISyntaxException {
        //given
        FriendsIdsData checkIsFriendsData = new FriendsIdsData(idFirstUser, idSecondUser);
        // when
        Mono<Result<IsFriends>> isFriendsMono =  friendPort.isFriends(Mono.just(checkIsFriendsData));
        // then
        StepVerifier.create(isFriendsMono)
                .expectNextMatches(result -> !result.getValue().areFriends())
                .expectComplete()
                .verify();
    }
}
