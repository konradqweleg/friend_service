package com.example.friends_service.integration.http;

import com.example.friends_service.integration.DefaultTestConfiguration;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

public class GetFriendsHttpTest  extends DefaultTestConfiguration {
    @Test
    public void whenUserHasNoFriendsRequestShouldReturnEmptyList() throws URISyntaxException {
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
