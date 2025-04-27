package com.example.friends_service.ut;

import com.example.friends_service.exceptions.FriendRelationAlreadyExists;
import com.example.friends_service.exceptions.UserNotFoundException;
import com.example.friends_service.exceptions.UserToCreateRelationNotFoundException;
import com.example.friends_service.model.api_models.FriendRelationDto;
import com.example.friends_service.model.api_models.IdUserDto;
import com.example.friends_service.model.api_models.UserDataDto;
import com.example.friends_service.port.out.persistence.PersistencePort;
import com.example.friends_service.port.out.services.UserServicePort;
import com.example.friends_service.services.FriendServices;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateFriendsTests {
    @MockBean
    private PersistencePort persistencePort;

    @MockBean
    private UserServicePort userServicePort;

    @Autowired
    private FriendServices friendServices;

    @Test
    void whenUsersAreFriendsShouldReturnThatUsersAreFriends() {
        //given
        IdUserDto idFirstFriend = new IdUserDto(1L);
        UserDataDto userFirst = new UserDataDto(1L, "John", "Doe", "mail@example.com");

        IdUserDto idSecondFriend = new IdUserDto(2L);
        UserDataDto userSecond = new UserDataDto(2L, "Adam", "Sandler", "mail2@example.com");

        FriendRelationDto friendRelation = new FriendRelationDto(idFirstFriend.idUser(), idSecondFriend.idUser());
        when(persistencePort.findFriendsRelation(friendRelation)).thenReturn(Mono.empty());
        when(userServicePort.getUserAboutId(idFirstFriend)).thenReturn(Mono.just(userFirst));
        when(userServicePort.getUserAboutId(idSecondFriend)).thenReturn(Mono.just(userSecond));
        when(persistencePort.createFriend(friendRelation)).thenReturn(Mono.empty());

        //when
        Mono<Void> result = friendServices.createFriends(friendRelation);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyComplete();

        Mockito.verify(persistencePort).createFriend(friendRelation);
    }

    @Test
    void whenFirstUserInNewPotentialRelationDoesNotExistsShouldNotCreateRelation(){
        IdUserDto idNoExistsUser = new IdUserDto(1L);

        IdUserDto idSecondFriend = new IdUserDto(2L);
        UserDataDto userSecond = new UserDataDto(2L, "Adam", "Sandler", "mail2@example.com");

        FriendRelationDto friendRelation = new FriendRelationDto(idNoExistsUser.idUser(), idSecondFriend.idUser());
        when(persistencePort.findFriendsRelation(friendRelation)).thenReturn(Mono.empty());
        when(userServicePort.getUserAboutId(idNoExistsUser)).thenReturn(Mono.error(new UserNotFoundException("User not found")));
        when(userServicePort.getUserAboutId(idSecondFriend)).thenReturn(Mono.just(userSecond));
        when(persistencePort.createFriend(friendRelation)).thenReturn(Mono.empty());

        //when
        Mono<Void> result = friendServices.createFriends(friendRelation);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyError(UserToCreateRelationNotFoundException.class);

        Mockito.verify(persistencePort, Mockito.never()).createFriend(friendRelation);
    }

    @Test
    void whenSecondUserInNewPotentialRelationDoesNotExistsShouldNotCreateRelation(){
        IdUserDto idFirstUser = new IdUserDto(1L);
        UserDataDto firstUser = new UserDataDto(1L, "Adam", "Sandler", "mail2@example.com");

        IdUserDto idNoExistsUser = new IdUserDto(2L);
        FriendRelationDto friendRelation = new FriendRelationDto(idNoExistsUser.idUser(), idFirstUser.idUser());
        when(persistencePort.findFriendsRelation(friendRelation)).thenReturn(Mono.empty());
        when(userServicePort.getUserAboutId(idFirstUser)).thenReturn(Mono.just(firstUser));
        when(userServicePort.getUserAboutId(idNoExistsUser)).thenReturn(Mono.error(new UserNotFoundException("User not found")));
        when(persistencePort.createFriend(friendRelation)).thenReturn(Mono.empty());

        //when
        Mono<Void> result = friendServices.createFriends(friendRelation);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                .verifyError(UserToCreateRelationNotFoundException.class);

        Mockito.verify(persistencePort, Mockito.never()).createFriend(friendRelation);
    }


    @Test
    void whenUsersAreAlreadyFriendsShouldCannotCreateAgainRelation() {
        //given
        IdUserDto idFirstFriend = new IdUserDto(1L);
        UserDataDto userFirst = new UserDataDto(1L, "John", "Doe", "mail@example.com");

        IdUserDto idSecondFriend = new IdUserDto(2L);
        UserDataDto userSecond = new UserDataDto(2L, "Adam", "Sandler", "mail2@example.com");

        FriendRelationDto friendRelation = new FriendRelationDto(idFirstFriend.idUser(), idSecondFriend.idUser());
        when(persistencePort.findFriendsRelation(friendRelation)).thenReturn( Mono.just(friendRelation));
        when(userServicePort.getUserAboutId(idFirstFriend)).thenReturn(Mono.just(userFirst));
        when(userServicePort.getUserAboutId(idSecondFriend)).thenReturn(Mono.just(userSecond));
        when(persistencePort.createFriend(friendRelation)).thenReturn(Mono.empty());

        //when
        Mono<Void> result = friendServices.createFriends(friendRelation);

        //then
        StepVerifier.create(result)
                .expectSubscription()
                        .verifyError(FriendRelationAlreadyExists.class);

        Mockito.verify(persistencePort, Mockito.never()).createFriend(friendRelation);
    }
}
