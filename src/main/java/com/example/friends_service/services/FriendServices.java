package com.example.friends_service.services;

import com.example.friends_service.model.api_models.IdUserDto;
import com.example.friends_service.model.api_models.IsFriendsDto;
import com.example.friends_service.exceptions.UserNotFoundException;

import com.example.friends_service.exceptions.UserToCreateRelationNotFoundException;
import com.example.friends_service.model.api_models.FriendRelationDto;
import com.example.friends_service.model.api_models.UserDataDto;
import com.example.friends_service.port.in.FriendPort;
import com.example.friends_service.port.out.services.UserServicePort;
import com.example.friends_service.port.out.persistence.PersistencePort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FriendServices implements FriendPort {
    private final PersistencePort persistencePort;
    private final UserServicePort userServicePort;
    private final Logger logger = LogManager.getLogger(FriendServices.class);

    public FriendServices(PersistencePort persistencePort, UserServicePort userServicePort) {
        this.persistencePort = persistencePort;
        this.userServicePort = userServicePort;
    }

    @Override
    public Mono<IsFriendsDto> isFriends(FriendRelationDto friendsRelationToFind) {
        return persistencePort.findFriendsRelation(friendsRelationToFind)
                .map(friend -> new IsFriendsDto(true))
                .defaultIfEmpty(new IsFriendsDto(false));
    }


    @Override
    public Mono<Void> createFriends(FriendRelationDto friendRelationToCreate) {
        IdUserDto idFirstFriend = new IdUserDto(friendRelationToCreate.idFirstFriend());
        IdUserDto idSecondFriend = new IdUserDto(friendRelationToCreate.idSecondFriend());

        return userServicePort.getUserAboutId(idFirstFriend)
                .flatMap(userFirst -> userServicePort.getUserAboutId(idSecondFriend)
                        .flatMap(userSecond -> persistencePort.findFriendsRelation(friendRelationToCreate)
                                .flatMap(foundFriendData -> Mono.justOrEmpty(foundFriendData)
                                        .switchIfEmpty(
                                                persistencePort.createFriend(
                                                        friendRelationToCreate
                                                )
                                        )
                                        .then())
                        )
                        .doOnError(UserNotFoundException.class, e -> {
                            logger.error("Second user in relation not found: {}", e.getMessage());
                            throw new UserToCreateRelationNotFoundException("Second user in relation not found");
                        })
                ).doOnError(UserNotFoundException.class, e -> {
                    logger.error("First user in relation not found: {}", e.getMessage());
                    throw new UserToCreateRelationNotFoundException("First user in relation not found");
                });


    }


    private boolean isTheSameUser(IdUserDto idUserDtoToFindFriends, Long idFriend) {
        return idUserDtoToFindFriends.idUser().equals(idFriend);
    }

    @Override
    public Flux<UserDataDto> getFriends(IdUserDto idUserDtoToFindFriends) {
        return userServicePort.getUserAboutId(idUserDtoToFindFriends)
                .thenMany(persistencePort.findUserFriends(idUserDtoToFindFriends))
                .flatMap(friendRelation -> {
                    IdUserDto friendId;
                    if (isTheSameUser(idUserDtoToFindFriends, friendRelation.idFirstFriend())) {
                        friendId = new IdUserDto(friendRelation.idSecondFriend());
                    } else {
                        friendId = new IdUserDto(friendRelation.idFirstFriend());
                    }

                    return userServicePort.getUserAboutId(friendId)
                            .map(user -> new UserDataDto(friendId.idUser(), user.name(), user.surname(), user.email()));
                });
    }

}
