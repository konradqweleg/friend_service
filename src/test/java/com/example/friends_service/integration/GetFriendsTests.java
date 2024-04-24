package com.example.friends_service.integration;

import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.integration.mocks.UserServicePortMock;
import com.example.friends_service.port.in.UserServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import java.net.URISyntaxException;


public class GetFriendsTests extends DefaultTestConfiguration {

    private final Long idFirstUser = 1L;
    private final Long idSecondUser = 2L;
    private final Long idThirdUser = 3L;

    @MockBean
    private UserServicePort userServicePort;
    private UserServicePortMock userServicePortMock = new UserServicePortMock();

    private void createFriends(Long idFirstUser, Long idSecondUser) throws URISyntaxException {
        FriendsIdsData friendsIdsData = new FriendsIdsData(idFirstUser, idSecondUser);
        webTestClient.post().uri(createRequestUtil().createRequestCreateFriends())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(friendsIdsData))
                .exchange();

    }
//    @Test public void whenUserHasFriendsRequestShouldReturnFriends() throws URISyntaxException {
//
//        // given
//        userServicePortMock.mockUserServicePortReturnUserForUserAboutIdsOneTwoAndThree(userServicePort);
//        createFriends(idFirstUser, idSecondUser);
//        createFriends(idFirstUser, idThirdUser);
//
//        // when
//        // then
//        webTestClient.get().uri(createRequestUtil().createRequestGetFriends(idFirstUser))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$[0].idFriend").isEqualTo(idSecondUser)
//                .jsonPath("$[1].idFriend").isEqualTo(idThirdUser);
//
//    }

    @Test public void whenUserHasNoFriendsRequestShouldReturnEmptyList() throws URISyntaxException {
        // given
        Long idUser = 1L;
        // when
        // then
        webTestClient.get().uri(createRequestUtil().createRequestGetFriends(idUser))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isEmpty();
    }
}
