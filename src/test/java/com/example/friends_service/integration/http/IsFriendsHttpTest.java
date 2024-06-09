package com.example.friends_service.integration.http;

import com.example.friends_service.integration.DefaultTestConfiguration;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

public class IsFriendsHttpTest extends DefaultTestConfiguration {

    @Test
    public void whenUsersAreNotFriendsShouldReturnUsersAreNotFriends() throws URISyntaxException {
        //given
        final Long idFirstUser = 1L;
        final Long idSecondUser = 2L;
        // when
        // then
        webTestClient.get().uri(createRequestUtil().createRequestIsFriends(idFirstUser, idSecondUser))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.areFriends").isEqualTo(false);
    }
}
