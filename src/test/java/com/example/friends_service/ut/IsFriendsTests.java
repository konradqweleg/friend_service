package com.example.friends_service.ut;

import com.example.friends_service.model.api_models.FriendRelationDto;
import com.example.friends_service.model.api_models.IsFriendsDto;
import com.example.friends_service.port.out.persistence.PersistencePort;
import com.example.friends_service.services.FriendServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IsFriendsTests {

    @MockBean
    private PersistencePort persistencePort;

    @Autowired
    private FriendServices friendServices;

    @Test
    void whenUsersAreFriendsShouldReturnThatUsersAreFriends() {
        //given
        FriendRelationDto friendRelation = new FriendRelationDto(1L, 2L);
        when(persistencePort.findFriendsRelation(friendRelation)).thenReturn(Mono.just(friendRelation));

        //when
        Mono<IsFriendsDto> result = friendServices.isFriends(friendRelation);

        //then
        assertDoesNotThrow(() -> result.block());
        assertEquals(new IsFriendsDto(true), result.block());
    }

    @Test
    void whenUsersAreNotFriendsShouldReturnThatUsersAreNotFriends() {
        //given
        FriendRelationDto friendRelation = new FriendRelationDto(1L, 2L);
        when(persistencePort.findFriendsRelation(friendRelation)).thenReturn(Mono.empty());

        //when
        Mono<IsFriendsDto> result = friendServices.isFriends(friendRelation);

        //then
        assertDoesNotThrow(() -> result.block());
        assertEquals(new IsFriendsDto(false), result.block());
    }
}
