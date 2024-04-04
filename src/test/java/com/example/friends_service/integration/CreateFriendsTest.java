package com.example.friends_service.integration;

import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.entity.request.IdUserData;
import com.example.friends_service.entity.request.UserData;
import com.example.friends_service.entity.response.Result;
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

public class CreateFriendsTest extends DefaultTestConfiguration{
    @MockBean
    private UserServicePort userServicePort;

    @Test
    public void createFriendsForCorrectDataShouldConnectUsersAsFriends() throws URISyntaxException {
        // given
        Long idFirstUser = 1L;
        Long idSecondUser = 2L;

        Mockito.when(userServicePort.getUserAboutId(any(Mono.class))).thenAnswer(invocation -> {
            Mono<IdUserData> monoIdUSer = invocation.getArgument(0);
            if (Objects.requireNonNull(monoIdUSer.block()).idUser() == 1L) {
                return Mono.just(Result.success(new UserData(idFirstUser, "User1", "User1", "User1")));
            }
            return Mono.just(Result.success(new UserData(idSecondUser, "User2", "User2", "User2")));
        });

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
                     new FriendsIdsData  (row.get("id_first_friend", Long.class),row.get("id_second_friend", Long.class)
                )).all();


        StepVerifier.create(friendsIdsDataFlux)
                .expectNextMatches(friends -> friends.idFirstFriend().equals(idFirstUser) && friends.idSecondFriend().equals(idSecondUser))
                .expectComplete()
                .verify();


    }
}
