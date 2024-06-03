package com.example.friends_service.integration;

import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.entity.request.IdUser;
import com.example.friends_service.entity.request.UserData;
import com.example.friends_service.integration.mocks.UserServicePortMock;
import com.example.friends_service.port.in.FriendPort;
import com.example.friends_service.port.in.UserServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URISyntaxException;
import java.util.List;


public class GetFriendsTests extends DefaultTestConfiguration {

    @MockBean
    private UserServicePort userServicePort;

    @Autowired
    private FriendPort friendPort;
    private final UserServicePortMock userServicePortMock = new UserServicePortMock();

    private void createFriends(Long idFirstUser, Long idSecondUser) throws URISyntaxException {
        FriendsIdsData friendsIdsData = new FriendsIdsData(idFirstUser, idSecondUser);
        webTestClient.post().uri(createRequestUtil().createRequestCreateFriends())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(friendsIdsData))
                .exchange();

    }
    @Test public void whenUserHasFriendsRequestShouldReturnFriends() throws URISyntaxException {

        // given
        userServicePortMock.mockUserServicePortReturnUserForUserAboutIdsOneTwoAndThree(userServicePort);
        Long idFirstUser = 1L;
        Long idSecondUser = 2L;
        Long idThirdUser = 3L;
        createFriends(idFirstUser, idSecondUser);
        createFriends(idFirstUser, idThirdUser);

        // when
        // then
        webTestClient.get().uri(createRequestUtil().createRequestGetFriends(idFirstUser))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    // Print the JSON response
                    System.out.println(new String(response.getResponseBody()));
                })
                .jsonPath("$[0].id").isEqualTo(idSecondUser)
                .jsonPath("$[1].id").isEqualTo(idThirdUser);

    }



    @Test public void whenUserHasNoFriendsRequestShouldReturnEmptyList() throws URISyntaxException {
        // given
        Long idUser = 1L;
        userServicePortMock.mockGetUserAboutIdForUserAboutIdOneAndTwo(userServicePort);

        // when
        Flux<UserData> friends = friendPort.getFriends(Mono.just(new IdUser(idUser)));
        // then
        StepVerifier.create(friends)
                .expectNextCount(0L)
                .expectComplete()
                .verify();


    }
}
