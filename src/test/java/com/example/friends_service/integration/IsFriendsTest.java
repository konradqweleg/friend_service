package com.example.friends_service.integration;

import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.integration.mocks.UserServicePortMock;
import com.example.friends_service.port.in.UserServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import java.net.URISyntaxException;


public class IsFriendsTest extends DefaultTestConfiguration {

    private final Long idFirstUser = 1L;
    private final Long idSecondUser = 2L;

    @MockBean
    private UserServicePort userServicePort;

    private UserServicePortMock userServicePortMock = new UserServicePortMock();
    @Test
    public void whenUsersAreFriendsShouldReturnUsersAreFriends() throws URISyntaxException {
        // given
        userServicePortMock.mockGetUserAboutIdForUserAboutIdOneAndTwo(userServicePort);

        FriendsIdsData friendsIdsData = new FriendsIdsData(idFirstUser, idSecondUser);
        webTestClient.post().uri(createRequestUtil().createRequestCreateFriends())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(friendsIdsData))
                .exchange();

        // when
        // then
        webTestClient.get().uri(createRequestUtil().createRequestIsFriends(idFirstUser, idSecondUser))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.areFriends").isEqualTo(true);

    }

    @Test
    public void whenUsersAreNotFriendsShouldReturnUsersAreNotFriends() throws URISyntaxException {

        // when
        // then
        webTestClient.get().uri(createRequestUtil().createRequestIsFriends(idFirstUser, idSecondUser))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.areFriends").isEqualTo(false);
    }
}
