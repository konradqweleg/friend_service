package com.example.friends_service.ut;

import com.example.friends_service.exceptions.UserNotFoundException;
import com.example.friends_service.model.api_models.FriendRelationDto;
import com.example.friends_service.model.api_models.IdUserDto;
import com.example.friends_service.model.api_models.UserDataDto;
import com.example.friends_service.port.out.persistence.PersistencePort;
import com.example.friends_service.port.out.services.UserServicePort;
import com.example.friends_service.services.FriendServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetUserFriendsTests {

    @MockBean
    private PersistencePort persistencePort;

    @MockBean
    private UserServicePort userServicePort;

    @Autowired
    private FriendServices friendServices;

    @Test
    void whenUserExistsAndHasFriendsShouldReturnFriendsList() {
        // given
        IdUserDto userId = new IdUserDto(1L);
        UserDataDto user = new UserDataDto(1L, "John", "Doe", "john.doe@example.com");

        FriendRelationDto friendRelation1 = new FriendRelationDto(1L, 2L);
        FriendRelationDto friendRelation2 = new FriendRelationDto(1L, 3L);

        UserDataDto friend1 = new UserDataDto(2L, "Jane", "Smith", "jane.smith@example.com");
        UserDataDto friend2 = new UserDataDto(3L, "Bob", "Brown", "bob.brown@example.com");

        when(userServicePort.getUserAboutId(userId)).thenReturn(Mono.just(user));
        when(persistencePort.findUserFriends(userId)).thenReturn(Flux.just(friendRelation1, friendRelation2));
        when(userServicePort.getUserAboutId(new IdUserDto(2L))).thenReturn(Mono.just(friend1));
        when(userServicePort.getUserAboutId(new IdUserDto(3L))).thenReturn(Mono.just(friend2));

        // when
        Flux<UserDataDto> result = friendServices.getFriends(userId);

        // then
        StepVerifier.create(result)
                .expectNext(friend1, friend2)
                .verifyComplete();
    }

    @Test
    void whenUserDoesNotExistShouldThrowUserNotFoundException() {
        // given
        IdUserDto userId = new IdUserDto(1L);
        when(userServicePort.getUserAboutId(userId)).thenReturn(Mono.error(new UserNotFoundException("User not found")));

        // when
        Flux<UserDataDto> result = friendServices.getFriends(userId);

        // then
        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();
    }

    @Test
    void whenUserExistsButHasNoFriendsShouldReturnEmptyList() {
        // given
        IdUserDto userId = new IdUserDto(1L);
        UserDataDto user = new UserDataDto(1L, "John", "Doe", "john.doe@example.com");

        when(userServicePort.getUserAboutId(userId)).thenReturn(Mono.just(user));
        when(persistencePort.findUserFriends(userId)).thenReturn(Flux.empty());

        // when
        Flux<UserDataDto> result = friendServices.getFriends(userId);

        // then
        StepVerifier.create(result)
                .verifyComplete();
    }
}
