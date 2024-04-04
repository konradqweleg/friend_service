package com.example.friends_service.integration;

import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.entity.request.IdUserData;
import com.example.friends_service.entity.request.UserData;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.integration.mocks.UserServicePortMock;
import com.example.friends_service.port.in.UserServicePort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URISyntaxException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;

public class CreateFriendsTest extends DefaultTestConfiguration {
    @MockBean
    private UserServicePort userServicePort;
    private UserServicePortMock userServicePortMock = new UserServicePortMock();
    private final Long idFirstUser = 1L;
    private final Long idSecondUser = 2L;

    @Test
    public void createFriendsForCorrectDataShouldConnectUsersAsFriends() throws URISyntaxException {
        // given
        userServicePortMock.mockGetUserAboutIdForUserAboutIdOneAndTwo(userServicePort);

        // when
        // then
        FriendsIdsData friendsIdsData = new FriendsIdsData(idFirstUser, idSecondUser);
        webTestClient.post().uri(createRequestUtil().createRequestCreateFriends())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(friendsIdsData))
                .exchange()
                .expectStatus().isOk()
                .expectBody();


        String sqlSelectIdsAllFriends = "SELECT id_first_friend, id_second_friend FROM friends_schema.friends;";
        //then
        Flux<FriendsIdsData> friendsIdsDataFlux = databaseClient.sql(sqlSelectIdsAllFriends)
                .map((row, metadata) ->
                        new FriendsIdsData(row.get("id_first_friend", Long.class), row.get("id_second_friend", Long.class)
                        )).all();


        StepVerifier.create(friendsIdsDataFlux)
                .expectNextMatches(friends -> friends.idFirstFriend().equals(idFirstUser) && friends.idSecondFriend().equals(idSecondUser))
                .expectComplete()
                .verify();


    }

    @Test
    public void whenConnectAsFriendsAlreadyExistsRequestShouldReturnError() throws URISyntaxException {
        // given
        userServicePortMock.mockGetUserAboutIdForUserAboutIdOneAndTwo(userServicePort);

        FriendsIdsData friendsIdsData = new FriendsIdsData(idFirstUser, idSecondUser);
        webTestClient.post().uri(createRequestUtil().createRequestCreateFriends())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(friendsIdsData))
                .exchange()
                .expectStatus().isOk()
                .expectBody();

        // when
        // then

        webTestClient.post().uri(createRequestUtil().createRequestCreateFriends())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(friendsIdsData))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.ErrorMessage").isEqualTo("Friend already exists");

    }

    @Test
    public void whenConnectAsFriendsWithNotExistingUserRequestShouldReturnError() throws URISyntaxException {
        // given
        userServicePortMock.mockGetUserAboutIdForUserAboutIdOneOtherNoExists(userServicePort);

        FriendsIdsData friendsIdsData = new FriendsIdsData(idFirstUser, idSecondUser);
        // when
        // then
        webTestClient.post().uri(createRequestUtil().createRequestCreateFriends())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(friendsIdsData))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.ErrorMessage").isEqualTo("User not found");

    }
}
